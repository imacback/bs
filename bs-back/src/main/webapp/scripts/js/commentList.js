App.commentList = function () {
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
                    url: appPath + '/comment/list.do'
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
                            name: 'bookId',
                            type: 'int'
                        },{
                            name: 'userId',
                            type: 'int'
                        },{
                            name: 'content',
                            type: 'string'
                        },{
                            name: 'isTop',
                            type: 'boolean'
                        },{
                        	name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: "editDate",
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: 'editorId',
                            type: 'int'
                        },{
                            name:'userName',
                            type:'string'
                        },{
                            name:'bookName',
                            type:'string'
                        },{
                            name: 'platformName',
                            type: 'string'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {

                var startCreateDate = App.dateFormat(Ext.getCmp("commentList.startCreateDate").getValue());
                var endCreateDate = App.dateFormat(Ext.getCmp("commentList.endCreateDate").getValue());

                //结束时间变为当天的最后时间
                if(null!=endCreateDate && ''!=endCreateDate && undefined!=endCreateDate){
                    endCreateDate = endCreateDate.replace("00:00:00","23:59:59");
                }

                Ext.apply(
                    thiz.baseParams,
                    {
                    	bookId: Ext.getCmp("commentList.bookId").getValue(),
                    	bookName: Ext.getCmp("commentList.bookName").getValue(),
                    	startCreateDate: startCreateDate,
                    	endCreateDate: endCreateDate,
                    	content: Ext.getCmp("commentList.content").getValue(),
                    	userName: Ext.getCmp("commentList.userName").getValue(),
                    	//userId是为了在用户管理页面的"评论"按钮使用的
                    	userId: Ext.getCmp("commentList.userId").getValue(),
                    	platformId: Ext.getCmp("commentList.platformId").getValue()
                    }
                );
            });
            return store;
        },

        //编辑评论信息的Form表单
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
            	url: appPath + '/comment/save.do',
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
                    },{//该ID用于信息的回显,修改时使用的是隐藏域的id,下边的disabled为true,值传不到后台
                        name: 'showId',
                        fieldLabel: '评论ID',
                        disabled:true
                    },{
                        name: 'bookId',
                        fieldLabel: '书ID',
                        disabled:true
                    },{
                        name: 'bookName',
                        fieldLabel: '书名',
                        disabled:true
                    },{
                        name: 'userName',
                        fieldLabel: '用户名',
                        disabled:true
                    },{
                        xtype: 'textarea',
                        id:'content',
                        name: 'content',
                        fieldLabel: '评论内容',
                        maxLength: 100,
                        allowBlank: false
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'isTop',
                        fieldLabel: '是否置顶',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['否', '0'],
                                ['是', '1']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    })
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
                                        App.commentList.dlg.hide();

                                        //刷新,重新加载列表信息
                                        App.commentList.store.reload();
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
                            //this.frm.form.reset();
                            //this.frm.form.clearInvalid();
                            Ext.getCmp("content").setValue('');
                        }
                    }
                ] //buttons
            }); //FormPanel

            return frm;
        },

        
        //用户评论的窗口
        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 300,
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
                    '书ID：',
                    {
                    	xtype: 'textfield',
                        id: 'commentList.bookId',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '书名：',
                    {
                    	xtype: 'textfield',
                        id: 'commentList.bookName',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '开始日期',
                    {
                        id: 'commentList.startCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                        //editable:false
                    },
                    {xtype: 'tbseparator'},
                    '结束日期',
                    {
                        id: 'commentList.endCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                        //editable:false
                    },
                    {xtype: 'tbseparator'},
                    '内容：',
                    {
                        xtype: 'textfield',
                        id: 'commentList.content',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '用户名：',
                    {
                        xtype: 'textfield',
                        id: 'commentList.userName',
                        width: 80
                    },
                    //userId是为了在用户管理页面中的"评论"按钮使用的
                    {
                        xtype: 'hidden',
                        id: 'commentList.userId',
                        value: ''
                    },
                    {xtype: 'tbseparator'},
                    '平台：',
                    new Ext.form.ComboBox({
                        id: "commentList.platformId",
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
                                this.add(newRecord);
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
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            //因为有可能在用户页面点击"评论"按钮后增加了userId的值,所以在搜索前先将userId情况(因为搜索区域没userId)
                            Ext.getCmp("commentList.userId").setValue('');

                            App.commentList.store.load();
                        }
                    }
                ]
            });
            
            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                //在后边的listeners里会将搜索工具条searchBar添加到tbar里
                tbar: [
                       /*
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '删除',
                        iconCls: 'icon-delete',
                        handler: this.del
                    }
                        */
                ],
                store: this.store,
                sm: sm,
                //columns: [sm, {
                columns: [{
                    header: "评论ID",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center'
                }, {
                    header: "书ID",
                    width: 60,
                    sortable: true,
                    dataIndex: 'bookId',
                    align: 'center'
                }, {
                    header: "书名",
                    width: 150,
                    sortable: true,
                    dataIndex: 'bookName',
                    align: 'center'
                },{
                    header: "用户名",
                    width: 150,
                    sortable: true,
                    dataIndex: 'userName',
                    align: 'center'
                }, {
                    header: "评论内容",
                    width: 250,
                    sortable: true,
                    dataIndex: 'content',
                    align: 'center'
                }, {
                    header: "平台",
                    width: 150,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                    //renderer: App.statusRender
                }, {
                    header: "添加时间",
                    width: 150,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },{
                    header: "置顶状态",
                    width: 80,
                    sortable: true,
                    dataIndex: 'isTop',
                    align: 'center',
                    renderer: App.statusRender
                },{
                    header: "置顶操作",
                    width: 80,
                    sortable: false,
                    dataIndex: 'id',
                    align: 'center',
                    renderer: topRender
                }, {
                    header: "操作",
                    width: 100,
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

            //置顶操作
            function topRender(value, p, record){
                //1表示已经置顶,0表示未置顶
                var isTop = record.data['isTop'];

                var topStr = '';
                if(isTop=='1'){
                    //topStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_down.png" title="撤销置顶" onclick="App.commentList.top(' + value + ')"/>';
                    //topStr = '<span onclick="App.commentList.top(' + value + ')" onMouseOver="App.commentList.mouseOver(this)">否</span>';
                    topStr = '<input type="button" value="取消" onclick="App.commentList.top(' + value + ')" onMouseOver="App.commentList.mouseOver(this)" style="width:40px;height:23px;"/>';
                }else{
                    //topStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_up.png" title="置顶" onclick="App.commentList.top(' + value + ')"/>';
                    //topStr = '<span onclick="App.commentList.top(' + value + ')" onMouseOver="App.commentList.mouseOver(this)">是</span>';
                    topStr = '<input type="button" value="置顶" onclick="App.commentList.top(' + value + ')" onMouseOver="App.commentList.mouseOver(this)" style="width:40px;height:23px;"/>';
                }
                return String.format(topStr);
            }

            //编辑,删除操作
            function optRender(value, p, record) {
            	var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.commentList.edit(' + value + ')"/>';
            	var deleteStr = '<input type="image" src="' + appPath + '/scripts/icons/delete.png" title="删除" onclick="App.commentList.deleteComment(' + value + ')"/>';

                return String.format(editStr + '&nbsp;&nbsp;&nbsp;&nbsp;' + deleteStr);
            }
            
            panel.add(this.grid);

        },
        /*
        add: function () {
            Ext.apply(App.commentList.currentFormValues, {
                id: '',
                name: "",
                nickname: "",
                email: "",
                roleId: "",
                isUse: ""
            });
            App.commentList.dlg.setTitle("增加后台用户");
            App.commentList.dlg.show();
        },

        del: function () {
            if (App.commentList.grid.getSelectionModel().hasSelection()) {
                var recs = App.commentList.grid.getSelectionModel().getSelections();
                var ids = [];
                var names = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    names += data.nickname + '<br>';
                }
                Ext.Msg.confirm('删除后台用户', '确定删除以下后台用户？<br><font color="red">' + names + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/adminUser/del.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.commentList.store.load();
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
                Ext.Msg.alert('信息', '请选择要删除的后台用户！');
            }
        },
        */

        //是否置顶操作的鼠标显示样式
        mouseOver: function(obj){
            obj.style.cursor='pointer';
        },
        
        //评论信息编辑
        edit: function() {
            if (App.commentList.grid.getSelectionModel().hasSelection()) {
                App.commentList.dlg.setTitle("编辑评论资料");
                var rec = App.commentList.grid.getSelectionModel().getSelected();

                //编辑窗口信息的回显
                Ext.apply(App.commentList.currentFormValues, {
                    id: rec.data.id,
                    showId: rec.data.id,//showId只用于显示用
                    bookId: rec.data.bookId,
                    bookName: rec.data.bookName,
                    userName: rec.data.userName,
                    content: App.commentList.HTMLDeCode(rec.data.content),//处理评论内容中的字符实体
                    isTop: rec.data.isTop
                });
                App.commentList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的评论。');
            }
        },

        //置顶
        top:function(){
            if (App.commentList.grid.getSelectionModel().hasSelection()) {
                var rec = App.commentList.grid.getSelectionModel().getSelected();
                var id = rec.data.id;
                var isTop = rec.data.isTop;
                var bookId = rec.data.bookId;
                var bookName = rec.data.bookName;

                //书籍为空的判断
                if(bookId==null || bookId==undefined || bookId==''){
                    Ext.Msg.alert('信息', '该评论未对应任何书籍，不能进行置顶操作！');
                    return;
                }

                var msg = '确定要置顶评论['+id+']?';
                var topStatus = 1;
                if(isTop){
                    msg = '确定要取消评论['+id+']的置顶?'
                    topStatus = 0;
                }

                Ext.Msg.confirm('信息提示',msg,function(btn){
                    if(btn=='yes'){
                        Ext.Ajax.request({
                            url: appPath + '/comment/setTop.do',
                            params: {id:id,topStatus:topStatus,bookId:bookId,bookName:bookName},
                            method: 'POST',
                            success: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                } else {
                                    Ext.Msg.alert('信息', info);
                                }
                                //重新加载列表信息
                                App.commentList.store.reload();
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
                        });
                    }
                },this);
            }else{
                Ext.Msg.alert('信息', '请选择要操作的评论。');
            }
        },
        
        //删除
        deleteComment:function(){
        	if (App.commentList.grid.getSelectionModel().hasSelection()) {
        		var rec = App.commentList.grid.getSelectionModel().getSelected();
        		var id = rec.data.id;
        		Ext.Msg.confirm('信息提示','确定要删除评论['+id+']?',function(btn){
        			if(btn=='yes'){
        				Ext.Ajax.request({
        					url: appPath + '/comment/del.do',
        					params: {id:id},
        	                method: 'POST',
        	                success: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                } else {
                                    Ext.Msg.alert('信息', info);
                                }
                                //刷新,重新加载列表信息
                                App.commentList.store.reload();
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
        	            });
        			}
        		},this);
            } else {
                Ext.Msg.alert('信息', '请选择要删除的评论。');
            }
        },

        //将串中的字符实体转成原来的显示值,如将"&gt;"转成">"
        HTMLDeCode:function(value){
            var s = "";
            if(value.length == 0) return s;
            s = value.replace(/&amp;/g, "&");
            s = s.replace(/&lt;/g, "<");
            s = s.replace(/&gt;/g, ">");
            s = s.replace(/&nbsp;/g, " ");
            s = s.replace(/&quot;/g, "\"");
            s = s.replace(/<br>/g, "\n");
            return s;
        },

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
        //该方法是在userList.js中调用,用户列表的"评论"按钮中使用到
        init: function (params) {
            Ext.getCmp("commentList.userId").setValue(params.userId);
            this.store.load();
            //App.commentList.grid.getBottomToolbar().store.load();
        },

        destroy: function() {}
    };
}();
