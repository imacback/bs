 
Ext.ns('App');

App.start_page_default = 0;
App.limit_page_default = 20;

App.previewUrl = '';
App.preview_height = 0;
App.preview_width = 0;
App.hasAuthority = false;

App.initMain = function () {
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    Ext.Msg.minWidth = 300;

    Ext.Ajax.on('requestcomplete', function (conn, response, options) {
        if (response && response.getResponseHeader && response.getResponseHeader('_timeout')) {
            Ext.MessageBox.confirm('请确认', '会话超时，请重新登录!!!', function (btn) {
                if (btn == 'yes') {
                    window.location = appPath + '/login.do';
                }
            });
        }
    }, this);

    App.createViewport();

    App.createMask();

    setTimeout(function () {
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({ remove: true });
    }, 250);
};

Ext.onReady(App.initMain);

App.createMask = function () {
    var mask = new Ext.LoadMask(Ext.getBody(), {
        msg: '数据处理中，请稍后...',
        removeMask: true //完成后移除
    });
    App.mask = mask;
    return mask;
};

App.createViewport = function () {
    var viewport = new Ext.Viewport({
        layout: 'border',
        items: [
            App.createNorth(),
            App.createWest(),
            App.createCenter()
        ]
    });
};

App.createNorth = function () {
    return {
        region: 'north',
        height: 28,
        margins: '5 5 5 5',
        bbar: [
                '<b style="font-size:12px;">欢迎您，<font color=red>' + userName + '</font></b>',
            '->', {xtype: 'tbtext', text: '暂时无新消息', id: 'createNorth.info', style: 'background-color:white; border:black solid 1px;'},
            {xtype: 'tbseparator'},
            {
                text: '修改密码',
                scope: this,
                iconCls: 'icon-key',
                handler: function () {
                    if (App.changePasswordDialog) {
                        App.changeInfo('修改密码', 'red');
                        App.changePasswordDialog.show();
                    }
                }
            }, {xtype: 'tbseparator'}, {
                text: '退出',
                iconCls: 'icon-disconnect',
                scope: this,
                handler: function () {
                    window.location = appPath + '/logout.do'
                }
            }]
    };
};
App.createWest = function () {
    Ext.Ajax.request({
        method: 'post',
        url: appPath + '/menu/list.do',
        success: function (resp, opts) {
            var result = Ext.util.JSON.decode(resp.responseText);
            if (result.success == 'true') {
                for (var i = 0; i < result.data.length; i++) {
                    var item = result.data[i];
                    var title = "<div>" + item.text + "</div>";

                    var node = new Ext.tree.TreePanel({
                        title: title,
                        border: true,
                        rootVisible: false,
                        lines: true,
                        autoScroll: true,
                        animate: true,
                        useArrows: false,
                        containerScroll: true,
                        singleClickExpand: true,
                        qtips: item.qitps,
                        root: {
                            editable: false,
                            expanded: true,
                            text: item.text,
                            draggable: false,
                            children: item.children
                        },
                        listeners: {
                            click: function (node, e) {
                                if (node.attributes.isLeaf == 1) {
                                    App.mainPanel.openTab(node);
                                } else {
                                    node.expandChildNodes();
                                }
                            }
                        }
                    });
                    Ext.getCmp('mainMenu').add(node);
                }
                Ext.getCmp('mainMenu').doLayout();
            } else {
                Ext.Msg.alert('提示', respText.info);
            }
        },
        failure: function (resp, opts) {
            //系统请求出错
            Ext.Msg.alert('提示', '系统请求错误！');
        }
    });
    return {
        id: 'mainMenu',
        region: 'west',
        layout: 'accordion',
        title: '功能菜单',
        split: true,
        width: 160,
        minSize: 175,
        maxSize: 400,
        collapsible: true,
        margins: '0 0 5 5',
        bodyStyle: 'padding: 2px',
        lines: true,
        autoScroll: true
    };
};

App.createCenter = function () {
    var mainPanel = new Ext.TabPanel({
        id: 'main-tabs',
        activeTab: 0,
        region: 'center',
        margins: '0 5 5 0',
        resizeTabs: true,
        tabWidth: 100,
        minTabWidth: 80,
        enableTabScroll: true,
        listeners: { beforeremove: function(tabpanel, tab){}},
        items: [
            {
                id: 'main-view',
                layout: 'fit',
                title: '欢迎您',
                closable: false,
                hideMode: 'offsets',
                html: "管理系统"
            }
        ]
    });

    mainPanel.openTab = function (node) {
        var id = node.attributes.url;
        var newTab = false;
        var tab = this.getItem(id);
        if (!tab) {
            tab = new Ext.Panel({
                id: id,
                title: node.text,
                tabTip: node.text,
                layout: 'fit',
                html: '<img alt="" src="' + appPath + '/scripts/extjs3/resources/images/default/shared/large-loading.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle"/>正在加载...',
                closable: true,
                autoScroll: true,
                border: false,
                bodyStyle: 'padding: 2px',
                listeners: {
                    'beforeclose':App.closeTab
                }
            });
            this.add(tab);
            newTab = true;
        }
        this.setActiveTab(tab);

        if (newTab) {
            if (App[id]) {
                App[id].render(id);
                this.doLayout();
            } else {
                Ext.Ajax.request({
                    panelId: id,
                    url: appPath + '/scripts/js/' + id + '.js',
                    success: function (response, opts) {
                        var o = eval(response.responseText);
                        if (o) {
                            App[id].render(opts.panelId);
                            if (node.params) {
                                   App[id].init(node.params);
                            }
                            this.doLayout();
                        }
                    },
                    failure: function () {
                        Ext.Msg.alert("错误", "加载模块失败！");
                    },
                    scope: this
                });
            }
        }
    };

    App.mainPanel = mainPanel;
    return mainPanel;
};
App.changeInfo = function (text, color) {
    Ext.getCmp('createNorth.info').setText('<font color=' + color + '>' + text + '</font>');
};
App.statusRender = function (value, p, record) {
    var img = value == 1 ? 'yes.png' : 'no.png';
    var alt = value == 1 ? '上架' : '下架';
    return String.format('<img src="' + appPath + '/scripts/extjs3/resources/images/default/dd/' + img + '" alt="' + alt + '" />');
};
App.yesRender = function (value, p, record) {
    var img = value == 1 ? 'yes.png' : 'no.png';
    var alt = value == 1 ? '是' : '否';
    return String.format('<img src="' + appPath + '/scripts/extjs3/resources/images/default/dd/' + img + '" alt="' + alt + '" />');
};
App.pushUserType = function (value, p, record) {
   
    if(value==0){
    	alt= "全部用户";
	}else if(value ==1){
		alt= "注册用户";
	}else if(value==2){
		alt= "非注册用户";
	}
    return String.format(alt);
};
App.subStr = function (value, p, record) {

    var alt="";
   if(value.length>50){
       alt =  value.substring(0,20);

   }else
   {
      alt =value;
   }

  return String.format(alt);
};

App.zeroRenderAll = function (value, p, record) {
    if(value==0){
    	alt= "全部";
	}else{
	    alt = value;
	 }
    return String.format(alt);
};
App.imgRender = function (value, p, record) {
    if (!value) return "";
    return String.format('<img src="' + imgUrl + '/' + value + '" alt="' + value + '" />');
};
App.imageRender = function (value, p, record) {
    if (!value) return "";
    return String.format('<img src="' + value + '" alt="' + value + '" />');
};
App.thumbnailRender = function (value, p, record) {
    if (!value) return "";
    return String.format('<img style="max-width:128px;" src="' + value + '" alt="' + value + '" />');
};
var record = Ext.data.Record.create([
    {name: 'id', type: 'int', mapping: '0'},
    {name: 'name', type: 'string', mapping: '1'}
]);
var allRecord = new record({id: '', name: '全部'});
var newRecord = new record({id: '0', name: '全部'});
//var selectRecord = new record({id: '0', name: '全部'});
var zeroRecord = new record({id: '0', name: '无'});
App.dateFormat = function dateFormat(value) {
    if (value) {
        return Ext.util.Format.date(new Date(value), 'Y-m-d H:i:s');
    } else {
        return null;
    }
};
App.ckeckLength = function chklen(value, len) {
    value = value.replace(/(^\s*)|(\s*$)/g, "");
    var length = value.replace(/[^\x00-\xff]/g, "**").length;
    if (length > len) {
        Ext.Msg.alert('提示:', "抱歉，输入内容长度不能大于" + len + "个字符(一个中文等于两个字符)");
        return false;
    }

    return true;
};
App.isNumber = function(s) {
    var regu = "^[0-9]+$";
    var re = new RegExp(regu);
    if (s.search(re) != -1) {
        return true;
    } else {
        return false;
    }
};
App.replaceAll = function(value, reallyDo, replaceWith) {
    return value.replace(new RegExp(reallyDo, "gm"), replaceWith);
};
App.filterContentRender = function(value) {
    if (value != '') {
        return App.replaceAll(App.replaceAll(value, "}", ">"), "{", "<");
    }
};
App.closeTab = function(e) {
    App.mainPanel.remove(e, true);
    App[e.id].destroy();
    App[e.id] = null;
};
 
