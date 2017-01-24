App.roleList = function () {
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
                    url: appPath + '/role/list.do'
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
                            name: 'memo',
                            allowBlank: false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        //name: Ext.getCmp("roleList.name").getValue()
                    }
                );
            });

            return store;
        },

        getAuthorityStore: function () {
            var store = new Ext.data.Store({
                baseParams: {},
                autoLoad: true,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/authority/list.do'
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
                            name: "text",
                            allowBlank: false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/role/saveAuthorities.do',
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
                        name: 'roleId',
                        value: ''
                    },
                    {
                        xtype: 'fieldset',
                        id: 'roleList.authorityFieldSet',
                        fieldLabel: '权限',
                        labelWidth: 80,
                        collapsible: false,
                        autoHeight: true,
                        layout: 'column',
                        style: 'border:0px;padding:0px;margin:0px;padding-top:0px;padding-left:2px;',
                        defaults: {
                            border: false,
                            anchor: '90%'
                        },
                        defaultType: 'checkbox',
                        items: []
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
                                        form.reset();
                                        App.roleList.dlg.hide();
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
                        handler: this.resetCheckGroup
                    }
                ] //buttons
            }); //FormPanel

            return frm;
        },

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 580,
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
                                id: 'roleList.name',
                                width: 80
                            },
                            {xtype: 'tbseparator'},
                            {
                                xtype: 'button',
                                text: '查找',
                                iconCls: 'icon-search',
                                handler: function () {
                                    App.roleList.store.load();
                                }
                            }
                        ]
                    }
                )
                ;

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                store: this.store,
                columns: [
                    {
                        header: "ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center'
                    },
                    {
                        header: "名称",
                        width: 100,
                        sortable: true,
                        dataIndex: 'name',
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
                        //searchBar.render(this.tbar);
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
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="管理权限" ondblclick="App.roleList.doubleClick()" onclick="App.roleList.editAuthority(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        doubleClick: function() {
            return;
        },

        editAuthority: function (value) {
            var requestConfig = {
                url: appPath + '/authority/getAuthorityIdsByRoleId.do?roleId=' + value,
                callback: function (o, s, r) {
                    var authorities = '';
                    var result = Ext.util.JSON.decode(r.responseText);
                    Ext.apply(App.roleList.currentFormValues, {
                        roleId: value,
                        authorities: result.result
                    });
                    if (result.result && result.result.length > 0) {
                        authorities = result.result;
                    }
                    App.roleList.buildCheckGroup(authorities);
                    App.roleList.dlg.setTitle('管理角色权限')
                    App.roleList.dlg.show();
                }
            };
            Ext.Ajax.request(requestConfig);
        },

        buildCheckGroup: function (values) {
            var ids = values.split(',');
            var length = ids.length;
            var size = this.authorityStore.getTotalCount();
            var fieldSet = Ext.getCmp("roleList.authorityFieldSet");
            fieldSet.removeAll();

            for (var i = -1; i < size; i++) {
                var moduleId = '';
                var moduleName = '';
                var checkboxId = '';
                var checked = false;
                var hidden = true;
                if (i > -1) {
                    hidden = false;
                    checkboxId = i;
                    moduleId = this.authorityStore.getAt(i).data.id;
                    moduleName = this.authorityStore.getAt(i).data.text;
                    if (length > 0) {
                        for (var j = 0; j < length; j++) {
                            if (ids[j] == moduleId) {
                                checked = true;
                            }
                        }
                    }
                }

                var checkboxModule = new Ext.form.Checkbox({
                    checked: checked,
                    width: 120,
                    id: "boxModule_" + checkboxId,
                    name: "authorities",
                    boxLabel: moduleName,
                    labelSeparator: "",  // 当没有标题时，不要 “:” 号，不要标题分隔
                    inputValue: moduleId,
                    hidden: hidden
                });

                fieldSet.add(checkboxModule); // 这里我是一个 fieldSet ，你也可以是一个 form 或者别的东西。
            }
        },

        resetCheckGroup: function() {
            var size = this.authorityStore.getTotalCount();
            for (var i = -1; i < size; i++) {
                var checkbox = Ext.getCmp("boxModule_"+i);
                if (checkbox){
                    checkbox.setValue(false);
                }
            }
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.authorityStore) {
                this.authorityStore = this.getAuthorityStore();
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
    }
}
();
