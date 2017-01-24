App.containerList = function () {
    return {

        currentFormValues: {},
        temporaryValue: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/container/list.do'
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
                            name: "isUse",
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
                        name: Ext.getCmp("containerList.name").getValue(),
                        isUse: Ext.getCmp("containerList.isUse").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/container/save.do',
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
                    //隐藏项,页面默认属于ID为1的站点
                    {
                        xtype: 'hidden',
                        name: 'siteId',
                        value: '1'
                    },
                    {
                        name: 'name',
                        fieldLabel: '名称',
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
                                        App.containerList.store.reload();
                                        App.containerList.dlg.hide();
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
                height: 150,
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
                                id: 'containerList.name',
                                width: 80
                            },
                            {
                                xtype: 'tbseparator'
                            }
                            ,
                            '状态：',
                            new Ext.form.ComboBox({
                                id: 'containerList.isUse',
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
                                    App.containerList.store.load();
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
                    }
                ],
                store: this.store,
                columns: [
                    {
                        header: "ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "名称",
                        width: 150,
                        sortable: true,
                        dataIndex: 'name',
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
                        header: "是否发布",
                        width: 80,
                        sortable: true,
                        dataIndex: 'status',
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
                        width: 100,
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
                var status = record.data.status;
                var isUse = record.data.isUse;
                var agentId = record.data.agentId;
                if (agentId == '') {
                    agentId = 0;
                }
                var html = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.containerList.edit(\'' + value + '\')"/>';
                html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/table_add.png" title="管理数据" onclick="App.containerList.showData(\'' + value + '\','+ agentId +')"/>';
                html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/eye.png" title="预览" onclick="App.containerList.preview(\'' + value + '\')"/>';
                html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/database_refresh.png" title="发布" onclick="App.containerList.publish(\'' + value + '\')"/>';
                return String.format(html);
            }

            panel.add(this.grid);

        },

        add: function () {
            Ext.apply(App.containerList.currentFormValues, {
                id: '',
                name: "",
                memo: "",
                isUse: ""
            });
            App.containerList.dlg.setTitle("增加页面");
            App.containerList.dlg.show();
        },

        edit: function () {
            if (App.containerList.grid.getSelectionModel().hasSelection()) {
                App.containerList.dlg.setTitle("编辑页面");
                var rec = App.containerList.grid.getSelectionModel().getSelected();
                Ext.apply(App.containerList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    agentId: rec.data.agentId,
                    isUse: rec.data.isUse
                });
                App.containerList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的页面。');
            }
        },

        preview: function (containerId) {
            window.open(appPath + '/container/preview.do?containerId='+containerId,'','width=325,scrollbars=yes,status=no');
        },

        publish: function (containerId) {
            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function (btn) {
                if (btn == 'yes') {
                    var requestConfig = {
                        url: appPath + '/container/publish.do?containerId=' + containerId,
                        callback: function (o, s, r) {
                            var result = Ext.util.JSON.decode(r.responseText);
                            if (result.success == 'true') {
                                App.containerList.store.reload();
                            }
                            Ext.Msg.alert('提示', result.info);
                        }
                    }
                    Ext.Ajax.request(requestConfig);
                }
            });
        },

        showData: function(containerId) {
            if (App.componentList) {
                var params = {url:'componentList', containerId: containerId, isUse: 1};
                App.componentList.init(params);
                var tab = App.mainPanel.getItem('componentList');
                App.mainPanel.setActiveTab(tab);
            } else {
                var node = {attributes: {url: "componentList"}, text: "组件管理", params: {containerId: containerId, isUse: 1}};
                App.mainPanel.openTab(node);
            }
        },

        reload: function() {
            this.store.reload();
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
}
();
