App.adminUserList = function () {
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
                    url: appPath + '/adminUser/list.do'
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
                            name: 'nickname',
                            allowBlank: false
                        },
                        {
                            name: 'roleName',
                            allowBlank: false
                        },
                        {
                            name: 'email',
                            allowBlank: false
                        },
                        {
                            name: 'roleId',
                            type: 'int'
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
                        nickname: Ext.getCmp("adminUserList.nickname").getValue(),
                        roleId: Ext.getCmp("adminUserList.roleId").getValue(),
                        isUse: Ext.getCmp("adminUserList.isUse").getValue(),
                        email: Ext.getCmp("adminUserList.email").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/adminUser/save.do',
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
                        fieldLabel: '用户名',
                        maxLength: 12,
                        regex: /^[0-9a-zA-Z]+$/,
                        regexText: '只能输入字母数字',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("nickname");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'nickname',
                        fieldLabel: '昵称',
                        maxLength: 12,
                        regex: /^[0-9a-zA-Z\u4E00-\u9FA5]+$/,
                        regexText: '输入汉字数字或字母',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("email");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'email',
                        fieldLabel: '邮箱',
                        maxLength: 40,
                        regex: /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/,
                        regexText: '请输入邮箱',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("roleId");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'roleId',
                        fieldLabel: '角色',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['超级管理员', '1'],
                                ['管理员', '2']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
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
                                        App.adminUserList.store.reload();
                                        App.adminUserList.dlg.hide();
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
                height: 230,
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
                    '昵称：',
                    {
                        xtype: 'textfield',
                        id: 'adminUserList.nickname',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '邮箱：',
                    {
                        xtype: 'textfield',
                        id: 'adminUserList.email',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '角色：',
                    new Ext.form.ComboBox({
                        id: 'adminUserList.roleId',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['全部', ''],
                                ['超级管理员', '1'],
                                ['管理员', '2']
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
                    '状态：',
                    new Ext.form.ComboBox({
                        id: 'adminUserList.isUse',
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
                            App.adminUserList.store.load();
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
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '删除',
                        iconCls: 'icon-delete',
                        handler: this.del
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [sm, {
                    header: "序号",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center',
                    hideable: false
                }, {
                    header: "用户名",
                    width: 100,
                    sortable: true,
                    dataIndex: 'name',
                    align: 'center'
                }, {
                    header: "昵称",
                    width: 150,
                    sortable: true,
                    dataIndex: 'nickname',
                    align: 'center'
                }, {
                    header: "角色",
                    width: 100,
                    sortable: true,
                    dataIndex: 'roleName',
                    align: 'center'
                }, {
                    header: "邮箱",
                    width: 200,
                    sortable: true,
                    dataIndex: 'email',
                    align: 'center'
                }, {
                    header: "状态",
                    width: 80,
                    sortable: true,
                    dataIndex: 'isUse',
                    align: 'center',
                    renderer: App.statusRender
                }, {
                    header: "创建者",
                    width: 100,
                    sortable: true,
                    dataIndex: 'creatorName',
                    align: 'center'
                }, {
                    header: "创建时间",
                    width: 80,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                }, {
                    header: "编辑者",
                    width: 100,
                    sortable: true,
                    dataIndex: 'editorName',
                    align: 'center'
                }, {
                    header: "编辑时间",
                    width: 80,
                    sortable: true,
                    dataIndex: 'editDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                }, {
                    header: "操作",
                    width: 80,
                    sortable: false,
                    dataIndex: 'id',
                    align: 'center',
                    renderer: optRender
                }],
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
            	 
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.adminUserList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        add: function () {
            Ext.apply(App.adminUserList.currentFormValues, {
                id: '',
                name: "",
                nickname: "",
                email: "",
                roleId: "",
                isUse: ""
            });
            App.adminUserList.frm.form.findField("name").setReadOnly(false);
            App.adminUserList.dlg.setTitle("增加后台用户");
            App.adminUserList.dlg.show();
        },

        del: function () {
            if (App.adminUserList.grid.getSelectionModel().hasSelection()) {
                var recs = App.adminUserList.grid.getSelectionModel().getSelections();
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
                                        App.adminUserList.store.load();
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

        edit: function () {
            if (App.adminUserList.grid.getSelectionModel().hasSelection()) {
                App.adminUserList.dlg.setTitle("编辑后台用户");
                var rec = App.adminUserList.grid.getSelectionModel().getSelected();
                Ext.apply(App.adminUserList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    nickname: rec.data.nickname,
                    email: rec.data.email,
                    isUse: rec.data.isUse,
                    roleId: rec.data.roleId
                });
                App.adminUserList.frm.form.findField("name").setReadOnly(true);
                //name
                App.adminUserList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的后台用户。');
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

        destroy: function() {
            this.store.destroy();
            this.frm.destroy();
            this.dlg.destroy();
        }
    };
}();
