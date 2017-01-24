App.entryList = function () {
    return {

        currentFormValues: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/clientEntry/list.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'id',
                            type: 'int'
                        },{
                            name: 'entryType',
                            allowBlank: false
                        },{
                            name: 'entryTypeName',
                            allowBlank: false
                        },{
                            name: "platformId",
                            type: 'int'
                        },{
                            name:"platformName"
                        },{
                            name: 'versions'
                        },{
                        	name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                var startCreateDate = App.dateFormat(Ext.getCmp("entryList.startCreateDate").getValue());
                var endCreateDate = App.dateFormat(Ext.getCmp("entryList.endCreateDate").getValue());

                //结束时间变为当天的最后时间
                if(null!=endCreateDate && ''!=endCreateDate && undefined!=endCreateDate){
                    endCreateDate = endCreateDate.replace("00:00:00","23:59:59");
                }

                Ext.apply(
                    thiz.baseParams,
                    {
                    	platformId: Ext.getCmp("entryList.platformId").getValue(),
                    	versions: Ext.getCmp("entryList.versions").getValue(),
                    	startCreateDate: startCreateDate,
                        endCreateDate: endCreateDate
                    }
                );
            });
            return store;
        },

        //编辑用户信息的Form
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
            	url: appPath + '/clientEntry/save.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 80,
                defaultType: 'textfield',
                defaults: {
                    allowBlank: false,
                    anchor: '90%',
                    enableKeyEvents: true
                },
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id',
                        value: ''
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'entryType',
                        fieldLabel: '启动入口',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['书架', '1'],
                                ['书城', '2']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    new Ext.form.ComboBox({
                        //name: 'platformName',
                        hiddenName: 'platformId',
                        fieldLabel: '平台',
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: {isUse: 1},
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/platform/platformDtoList.do'
                            }),
                            remoteSort: false,
                            reader: new Ext.data.JsonReader({
                                totalProperty: 'totalItems',
                                root: 'result',
                                idProperty: 'id',
                                fields: ['id', 'name']
                            })
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        allowBlank: false,
                        editable: false
                    }),
                    {
                        name: 'versions',
                        fieldLabel: '版本号',
                        allowBlank:false,
                        emptyText: '多版本请用;隔开'
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function (){
                            if (this.frm.form.isValid()) {
                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.entryList.store.reload();
                                        App.entryList.dlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                    },
                                    waitMsg: '正在保存数据，稍后...'
                                });
                            }
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            this.frm.form.reset();
                            this.frm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return frm;
        },

        //用户编辑的窗口
        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 170,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.frm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.frm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.frm.form.setValues(this.currentFormValues);
                        this.frm.form.clearInvalid();
                    }
                }
            }); //dlg
            return dlg;
        },

        createGrid: function (id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            var sm = new Ext.grid.CheckboxSelectionModel();

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    '平台：',
                    new Ext.form.ComboBox({
                        id: "entryList.platformId",
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: {
                            	//可用的平台信息
                                isUse: 1
                            },
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/platform/platformDtoList.do'
                            }),
                            remoteSort: false,
                            reader: new Ext.data.JsonReader({
                                totalProperty: 'totalItems',
                                root: 'result',
                                idProperty: 'id',
                                fields: ['id', 'name']
                            }),
                            listeners: {'load': function () {
                                //"全部"选项
                                this.add(allRecord);
                            }
                            }
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false,
                        width: 100
                    }),
                    {xtype: 'tbseparator'},
                    '版本号：',
                    {
                    	xtype: 'textfield',
                        id: 'entryList.versions',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '创建时间开始：',
                    {
                        id: 'entryList.startCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                    },
                    {xtype: 'tbseparator'},
                    '创建时间结束：',
                    {
                        id: 'entryList.endCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.entryList.store.load();
                        }
                    }
                    /*
                    ,
                    {
                        xtype: 'button',
                        text: 'TEST',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.entryList.TEST();
                        }
                    }
                    '角色：',
                    new Ext.form.ComboBox({
                        id: 'entryList.roleId',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['全部', ''],
                                ['管理员', '1'],
                                ['操作员', '2']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        width: 80
                    })
                    */
                ]
            });
            
            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                //在后边的listeners里会将搜索工具条searchBar添加到tbar里
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量删除',
                        iconCls: 'icon-delete',
                        handler: this.del
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '发布所有配置',
                        iconCls: 'icon-database_refresh',
                        handler: this.publish
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [sm, {
                    header: "序号",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center'
                }, {
                    header: "启动入口",
                    width: 80,
                    sortable: true,
                    dataIndex: 'entryTypeName',
                    align: 'center'
                },{
                    header: "平台",
                    width: 80,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                },{
                    header: "版本号",
                    width: 100,
                    sortable: true,
                    dataIndex: 'versions',
                    align: 'center'
                }, {
                    header: "创建时间",
                    width: 80,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                },{
                    header: "操作",
                    width: 60,
                    sortable: false,
                    dataIndex: 'id',
                    align: 'center',
                    renderer: optRender
                }],
                listeners: {
                    'render': function () {
                    	//将搜索工具条添加到tbar后边(前边有可能有按钮工具条)
                        searchBar.render(this.tbar);
                    }
                },
                bbar: new Ext.PagingToolbar({
                    store: this.store,
                    pageSize: this.store.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            //操作
            function optRender(value, p, record) {
            	var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.entryList.edit(' + value + ')"/>';
                return String.format(editStr);
            }

            panel.add(this.grid);

        },

        //评论信息编辑
        edit: function() {
            if (App.entryList.grid.getSelectionModel().hasSelection()) {
                App.entryList.dlg.setTitle("编辑启动入口配置资料");
                var rec = App.entryList.grid.getSelectionModel().getSelected();
                //编辑窗口信息的回显
                Ext.apply(App.entryList.currentFormValues, {
                    id: rec.data.id,
                    entryType: rec.data.entryType,
                    platformId: rec.data.platformId,
                    versions: rec.data.versions
                });
                App.entryList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的启动入口配置。');
            }
        },

        //增加启动入口
        add: function () {
            Ext.apply(App.entryList.currentFormValues, {
                id: '',
                entryType: "",
                platformId: "",
                versions: ""
            });
            App.entryList.dlg.setTitle("增加启动入口配置");
            App.entryList.dlg.show();
        },

        //批量删除
        del: function () {
            if (App.entryList.grid.getSelectionModel().hasSelection()) {
                var recs = App.entryList.grid.getSelectionModel().getSelections();
                var ids = [];
                var names = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    names += data.id + '<br>';
                }
                Ext.Msg.confirm('删除章节', '确定删除以下启动入口配置？<br><div style="width:210px;height:70px;overflow:auto;"><font color="red">' + names + '</font></div>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/clientEntry/del.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.entryList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要删除的启动入口配置！');
            }
        },

        //发布方法,每次都是重新发布所有
        publish: function () {
            Ext.Msg.confirm('发布启动入口配置信息', '确定发布所有启动入口配置信息吗？',
                function (btn) {
                    if (btn == 'yes') {
                        Ext.Ajax.request({
                            method: 'post',
                            url: appPath + '/clientEntry/publish.do',
                            /*
                            params: {
                                ids: ids.toString()
                            },
                            */
                            success: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                    //App.entryList.store.load();
                                } else {
                                    Ext.Msg.alert('信息', info);
                                }
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
                        });
                    }
                });

        },

        /*
        TEST:function(){
            Ext.Ajax.request({
                method: 'post',
                url: appPath + '/clientEntry/TEST.do',
                params: {
                    platformId: 2,version:'4.8888'
                },
                success: function (resp, opts) {
                    var result = Ext.util.JSON.decode(resp.responseText);
                    var info = result.info;
                    if (result.success == 'true') {
                        Ext.Msg.alert('信息', info);
                    } else {
                        Ext.Msg.alert('信息', info);
                    }
                },
                failure: function (resp, opts) {
                    var result = Ext.util.JSON.decode(resp.responseText);
                    var info = result.info;
                    Ext.Msg.alert('提示', info);
                }
            });
        },
        */

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            }
            ;
            if (!this.frm) {
                this.frm = this.getForm();
            }
            ; //if(!this.frm)


            if (!this.dlg) {
                this.dlg = this.getDialog();
            }
            ; //if(!this.dlg)

            this.createGrid(id);

        },

        init: function(params) {},

        destroy: function() {}
    };
}();
