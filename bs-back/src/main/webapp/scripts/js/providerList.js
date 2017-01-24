App.providerList = function () {
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
                    url: appPath + '/provider/list.do'
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
                        },
                        {
                            name: 'name',
                            allowBlank: false
                        },
                        {
                            name: 'ip',
                            allowBlank: false
                        },
                        {
                            name: 'secretKey',
                            allowBlank: false
                        },
                        {
                            name: "bookCount",
                            type: 'int'
                        },
                        {
                            name: "onlineCount",
                            type: 'int'
                        },
                        {
                            name: "batchCount",
                            type: 'int'
                        },
                        {
                            name: "status",
                            type: 'int'
                        },
                        {
                            name: 'creatorName',
                            allowBlank: false
                        },
                        {
                            name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: 'editorName',
                            allowBlank: false
                        },
                        {
                            name: "editDate",
                            type: 'date',
                            dateFormat: 'time'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        name: Ext.getCmp("providerList.name").getValue(),
                        id: Ext.getCmp("providerList.id").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/provider/save.do',
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
                    {
                        name: 'name',
                        fieldLabel: '版权方',
                        maxLength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("IP");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'ip',
                        fieldLabel: 'IP',
                        maxLength: 100,
                        emptyText: '多个ip使用;分隔',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("status");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'status',
                        fieldLabel: '状态',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['正常', '1'],
                                ['禁用', '0']
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
                        handler: function () {
                            if (this.frm.form.isValid()) {
                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.providerList.store.reload();
                                        App.providerList.dlg.hide();
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
                height: 180,
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
                    '版权ID：',
                    {
                        xtype: 'textfield',
                        id: 'providerList.id',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '版权方：',
                    {
                        xtype: 'textfield',
                        id: 'providerList.name',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.providerList.store.load();
                        }
                    }
                ]
            });

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add
                    },
                    {xtype : 'tbseparator'},
                    {
                        text: '发布', iconCls: 'icon-database_refresh',
                        handler: function () {
                            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function(btn){
                                if (btn == 'yes') {
                                    var requestConfig = {
                                        url :  appPath+'/provider/publish.do',
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
                columns: [
                    {
                        header: "版权ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "版权方",
                        width: 150,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center'
                    },
                    {
                        header: "IP",
                        width: 200,
                        sortable: true,
                        dataIndex: 'ip',
                        align: 'center'
                    },
                    {
                        header: "密钥",
                        width: 200,
                        sortable: true,
                        dataIndex: 'secretKey',
                        align: 'center'
                    },
                    {
                        header: "书籍数量",
                        width: 80,
                        sortable: true,
                        dataIndex: 'bookCount',
                        align: 'center'
                    },
                    {
                        header: "上线书籍数量",
                        width: 100,
                        sortable: true,
                        dataIndex: 'bookCount',
                        align: 'center'
                    },
                    {
                        header: "批次数量",
                        width: 80,
                        sortable: true,
                        dataIndex: 'batchCount',
                        align: 'center'
                    },
                    {
                        header: "状态",
                        width: 80,
                        sortable: true,
                        dataIndex: 'status',
                        align: 'center',
                        renderer: App.statusRender
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
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.providerList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        add: function () {
            Ext.apply(App.providerList.currentFormValues, {
                id: '',
                name: "",
                status: "",
                ip: ""
            });
            App.providerList.dlg.setTitle("增加版权");
            App.providerList.dlg.show();
        },

        edit: function () {
            if (App.providerList.grid.getSelectionModel().hasSelection()) {
                App.providerList.dlg.setTitle("编辑版权");
                var rec = App.providerList.grid.getSelectionModel().getSelected();
                Ext.apply(App.providerList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    status: rec.data.status,
                    ip: rec.data.ip
                });
                App.providerList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的版权。');
            }
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            }
            if (!this.frm) {
                this.frm = this.getForm();
            }
            if (!this.dlg) {
                this.dlg = this.getDialog();
            }
            this.createGrid(id);
        },

        init: function(params) {},

        destroy: function(){
            this.store.destroy();
            this.frm.destroy();
            this.dlg.destroy();
        }
    };
}();
