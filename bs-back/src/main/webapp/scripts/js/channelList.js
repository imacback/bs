App.channelList = function () {
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
                    url: appPath + '/channel/list.do'
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
                            name: 'alias',
                            allowBlank: false
                        },
                        {
                            name: 'memo',
                            allowBlank: false
                        },
                        {
                            name: "isUse",
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
                        name: Ext.getCmp("channelList.name").getValue(),
                        isUse: Ext.getCmp("channelList.isUse").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/channel/save.do',
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
                        fieldLabel: '名称',
                        maxLength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("alias");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'alias',
                        fieldLabel: '简称',
                        maxLength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'memo',
                        fieldLabel: '描述',
                        maxLength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("isUse");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'isUse',
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
                                        App.channelList.store.reload();
                                        App.channelList.dlg.hide();
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
                height: 200,
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
                            '名称：',
                            {
                                xtype: 'textfield',
                                id: 'channelList.name',
                                width: 80
                            },
                            {
                                xtype: 'tbseparator'
                            }
                            ,
                            '状态：',
                            new Ext.form.ComboBox({
                                id: 'channelList.isUse',
                                store: new Ext.data.SimpleStore({
                                    fields: ['text', 'value'],
                                    data: [
                                        ['全部', ''],
                                        ['正常', '1'],
                                        ['禁用', '0']
                                    ]
                                }),
                                mode: 'local',
                                displayField: 'text',
                                valueField: 'value',
                                triggerAction: 'all',
                                editable: false,
                                width: 80
                            }),
                            {xtype: 'tbseparator'},
                            {
                                xtype: 'button',
                                text: '查找',
                                iconCls: 'icon-search',
                                handler: function () {
                                    App.channelList.store.load();
                                }
                            }
                        ]
                    }
                )
                ;

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
                                        url :  appPath+'/channel/publish.do',
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
                        header: "序号",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "名称",
                        width: 100,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center'
                    },
                    {
                        header: "简称",
                        width: 100,
                        sortable: true,
                        dataIndex: 'alias',
                        align: 'center'
                    },
                    {
                        header: "描述",
                        width: 150,
                        sortable: true,
                        dataIndex: 'memo',
                        align: 'center'
                    },
                    {
                        header: "状态",
                        width: 80,
                        sortable: true,
                        dataIndex: 'isUse',
                        align: 'center',
                        renderer: App.statusRender
                    },
                    {
                        header: "创建者",
                        width: 100,
                        sortable: true,
                        dataIndex: 'creatorName',
                        align: 'center'
                    },
                    {
                        header: "创建时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'createDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },
                    {
                        header: "编辑者",
                        width: 100,
                        sortable: true,
                        dataIndex: 'editorName',
                        align: 'center'
                    },
                    {
                        header: "编辑时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'editDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
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
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.channelList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        add: function () {
            Ext.apply(App.channelList.currentFormValues, {
                id: '',
                name: "",
                alias: "",
                memo: "",
                isUse: ""
            });
            App.channelList.dlg.setTitle("增加频道");
            App.channelList.dlg.show();
        },

        edit: function () {
            if (App.channelList.grid.getSelectionModel().hasSelection()) {
                App.channelList.dlg.setTitle("编辑频道");
                var rec = App.channelList.grid.getSelectionModel().getSelected();
                Ext.apply(App.channelList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    alias: rec.data.alias,
                    memo: rec.data.memo,
                    isUse: rec.data.isUse
                });
                App.channelList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的频道。');
            }
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

        init: function(params) {},

        destroy: function() {}
    }
}
();
