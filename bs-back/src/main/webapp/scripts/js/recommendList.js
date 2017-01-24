App.recommendList = function () {
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
                    url: appPath + '/recommend/list.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'bookId',
                            type: 'long'
                        },
                        {
                            name: 'bookName',
                            allowBlank: false
                        },
                        {
                            name: 'author',
                            allowBlank: false
                        },
                        {
                            name: "orderId",
                            type: 'int'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        bookId: Ext.getCmp("recommendList.bookId").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/recommend/save.do',
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
                        name: 'op',
                        value: ''
                    },
                    {
                        name: 'bookId',
                        fieldLabel: '书籍ID',
                        maxLength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.dataInputForm.form.findField("orderId");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'orderId',
                        fieldLabel: '排序号',
                        maxLength: 5,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.dataInputForm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            if (this.frm.form.isValid()) {
                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        App.recommendList.store.reload();
                                        App.recommendList.dlg.hide();
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

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 140,
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

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    '书籍ID：',
                    {
                        xtype: 'textfield',
                        id: 'recommendList.bookId',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.recommendList.store.load();
                        }
                    }
                ]
            });

            var sm = new Ext.grid.CheckboxSelectionModel();

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: function () {
                            App.recommendList.add();
                        }
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    {
                        xtype: 'button',
                        text: '删除',
                        iconCls: 'icon-delete',
                        handler: function () {
                            App.recommendList.del();
                        }
                    },
                    {xtype : 'tbseparator'},
                    {
                        text: '发布', iconCls: 'icon-database_refresh',
                        handler: function () {
                            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function(btn){
                                if (btn == 'yes') {
                                    var requestConfig = {
                                        url :  appPath+'/recommend/publish.do',
                                        callback:function(o, s, r) {
                                            var result = Ext.util.JSON.decode(r.responseText);
                                            Ext.Msg.alert('提示',result.info);
                                        }
                                    }
                                    Ext.Ajax.request(requestConfig);
                                }
                            });
                        }
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [sm,
                    {
                        header: "排序",
                        width: 60,
                        sortable: true,
                        dataIndex: 'orderId',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "书籍ID",
                        width: 100,
                        sortable: true,
                        dataIndex: 'bookId',
                        align: 'center'
                    },
                    {
                        header: "书名",
                        width: 200,
                        sortable: true,
                        dataIndex: 'bookName',
                        align: 'center'
                    },
                    {
                        header: "作者",
                        width: 100,
                        sortable: true,
                        dataIndex: 'author',
                        align: 'center'
                    },
                    {
                        header: "操作",
                        width: 80,
                        sortable: false,
                        dataIndex: 'id',
                        align: 'center',
                        renderer: optRender
                    }
                ],
                listeners: {
                    'render': function () {
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

            function optRender(value, p, record) {
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.recommendList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        add: function () {
            Ext.apply(App.recommendList.currentFormValues, {
                op: 'add',
                bookId: '',
                orderId: ""
            });
            App.recommendList.frm.form.findField("bookId").setReadOnly(false);
            App.recommendList.dlg.setTitle("增加推荐");
            App.recommendList.dlg.show();
        },

        edit: function () {
            if (App.recommendList.grid.getSelectionModel().hasSelection()) {
                App.recommendList.dlg.setTitle("编辑推荐");
                var rec = App.recommendList.grid.getSelectionModel().getSelected();
                Ext.apply(App.recommendList.currentFormValues, {
                    op: 'update',
                    bookId: rec.data.bookId,
                    orderId: rec.data.orderId
                });
                App.recommendList.frm.form.findField("bookId").setReadOnly(true);
                App.recommendList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的推荐数据。');
            }
        },

        del: function () {
            if (App.recommendList.grid.getSelectionModel().hasSelection()) {
                var recs = App.recommendList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.bookId);
                    words += data.bookName + '<br>';
                }
                Ext.Msg.confirm('删除数据', '确定删除以下推荐数据？<br><font color="red">' + words + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/recommend/del.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.recommendList.store.load();
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
                Ext.Msg.alert('信息', '请选择要删除的推荐数据！');
            }
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.frm) {
                this.frm = this.getForm();
            }; //if(!this.frm)

            if (!this.dlg) {
                this.dlg = this.getDialog();
            }; //if(!this.dlg)

            this.createGrid(id);
        },

        init: function(params) {},

        destroy: function() {}
    };
}();
