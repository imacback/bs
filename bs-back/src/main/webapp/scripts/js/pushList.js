App.pushList = function () {
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
                    url: appPath + '/push/list.do'
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
                            name: 'title',
                            allowBlank: false
                        },
                        {
                            name: 'content',
                            allowBlank: false
                        },
                        {
                            name: 'url',
                            allowBlank: false
                        },
                        {
                            name: 'status',
                            type: 'int'
                        },
                        {
                            name: "platformId",
                            type: 'int'
                        },
                        {
                            name: 'targetType',
                            type: 'int'
                        },
                        {
                            name: 'versions',
                            allowBlank: false
                        },
                        {
                            name: "createDate",
                            allowBlank: false
                        },
                        {
                            name: 'ditchIds',
                            allowBlank: false
                        },
                        {
                            name: "editDate",
                            allowBlank: false
                        }, {
                            name: "platformName",
                            type: 'String'
                        }, {
                            name: 'publishDate',
                            allowBlank: false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        platformId: Ext.getCmp("pushList.platformId").getValue(),
                        targetType: Ext.getCmp("pushList.targetType").getValue(),
                        status: Ext.getCmp("pushList.status").getValue(),
                        versions: Ext.getCmp("pushList.versions").getValue(),
                        ditchIds: Ext.getCmp("pushList.ditchIds").getValue(),
                        createDate: Ext.getCmp("pushList.createDate").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/push/save.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 120,
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
                        value: '0'
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'platformId',
                        fieldLabel: 'PUSH平台',
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
                        mode: 'local',
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        allowBlank: false,
                        editable: false
                    }),
                    new Ext.form.ComboBox({
                        hiddenName: 'targetType',
                        fieldLabel: 'PUSH对象',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [['全部', '0'], ['注册用户', '1'], ['非注册用户', '2']]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {
                        name: 'versions',
                        fieldLabel: '版本号(多个以;分隔)',
                        maxLength: 100,
                        maxLengthText: "超过最大输入值",
                        regex: /^[0-9;.]+$/,
                        regexText: '只能输入数字,逗号,分号',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("versions");
                                    if (obj) obj.focus();
                                }
                            }
                        }
                    },
                    {
                        name: 'ditchIds',
                        fieldLabel: '渠道号(多个以;分隔)',
                        maxLength: 100,
                        maxLengthText: "超过最大输入值",
                        regex: /^[0-9a-zA-Z;.]+$/,
                        regexText: '输入数字或字母分号分隔',
                        allowBlank: true
                    },
                    {
                        name: 'title',
                        fieldLabel: 'PUSH标题',
                        maxLength: 14,
                        maxLengthText: "超过最大输入值"
                    },
                    {
                        name: 'content',
                        fieldLabel: 'PUSH内容',
                        maxLength: 16,
                        maxLengthText: "超过最大输入值"

                    },
                    {
                        name: 'url',
                        fieldLabel: '链接资源',
                        allowBlank: true,
                        maxLength: 200
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'status',
                        fieldLabel: '状态',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['上线', '1'],
                                ['下线', '0']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    new Ext.ux.form.DateTimeField({
                        fieldLabel: '发布时间',
                        name: 'publishDate',
                        emptyText: '请选择',
                        format: 'Y-m-d H',
                        //enableKeyEvents: true,
                        allowBlank: false,
                        listeners: {
                            'select': function (datetitmepicker, date) {
                                var value = App.pushList.formatDate(date);
                                Ext.getCmp('push.publishDate').setValue(value);
                            }
                        }
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
                                        App.pushList.store.reload();
                                        App.pushList.dlg.hide();
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
                width: 550,
                height: 330,
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
                        id: 'pushList.platformId',
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
                            listeners: {
                                'load': function () {
                                    //"全部"选项
                                    this.add(allRecord);
                                }
                            }
                        }),
                        mode: 'local',
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {
                        xtype: 'tbseparator'
                    },
                    '渠道号：',
                    {
                        xtype: 'textfield',
                        id: 'pushList.ditchIds',
                        width: 80
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    '版本号：',
                    {
                        xtype: 'textfield',
                        id: 'pushList.versions',
                        width: 80
                    },

                    {
                        xtype: 'tbseparator'
                    },
                    'PUSH对象：',
                    new Ext.form.ComboBox({
                        id: 'pushList.targetType',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [['全部', ''], ['全部用户', '0'], ['注册用户', '1'], ['非注册用户', '2']]
                        }),
                        mode: 'local',
                        value: '',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {
                        xtype: 'tbseparator'
                    },
                    '创建日期：',
                    {
                        xtype: "datefield",
                        id: 'pushList.createDate',
                        format: 'y-m-d',
                        showToday: true,
                        width: 100
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    '状态：',
                    new Ext.form.ComboBox({
                        id: 'pushList.status',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [['全部', ''], ['下线', '0'],
                                ['上线', '1']]
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
                            App.pushList.store.load();
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
                    }, {
                        xtype: 'button',
                        text: '发布',
                        iconCls: 'icon-add',
                        handler: this.release
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
                    header: "PUSH标题",
                    width: 200,
                    sortable: true,
                    dataIndex: 'title',
                    align: 'center'
                }, {
                    header: "PUSH内容",
                    width: 200,
                    sortable: true,
                    dataIndex: 'content',
                    align: 'center'
                }, {
                    header: "平台",
                    width: 80,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                }, {
                    header: "版本",
                    width: 80,
                    sortable: true,
                    dataIndex: 'versions',
                    renderer: App.subStr,
                    align: 'center'
                }, {
                    header: "渠道号",
                    width: 80,
                    renderer: App.zeroRenderAll,
                    dataIndex: 'ditchIds',
                    renderer: App.subStr,
                    align: 'center'
                }, {
                    header: "PUSH对象",
                    width: 80,
                    dataIndex: 'targetType',
                    renderer: App.pushUserType,
                    align: 'center'
                }, {
                    header: "状态",
                    width: 60,
                    sortable: true,
                    dataIndex: 'status',
                    renderer: App.yesRender,
                    align: 'center'
                }, {
                    header: "创建日期",
                    width: 120,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center'
                }, {
                    header: "发布时间",
                    width: 120,
                    sortable: true,
                    dataIndex: 'publishDate',
                    align: 'center'
                }, {
                    header: "操作",
                    width: 60,
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
                var ca = '<input type="image" src="'
                    + appPath
                    + '/scripts/icons/page_edit.png" title="编辑" onclick="App.pushList.edit('
                    + value + ')"/>';
                var fb = '<input type="image" src="'
                    + appPath
                    + '/scripts/icons/page_go.png" title="发布" onclick="App.pushList.release('
                    + value + ')"/>';
                var b = "";
                if (record.data.status == '1') {
                    b = ' <input type="image" src="'
                    + appPath
                    + '/scripts/icons/page_white_put.png" title="下线" onclick="App.pushList.status_dow('
                    + value + ')"/>';

                } else if (record.data.status == '0') {
                    b = ca + '<input type="image" src="'
                    + appPath
                    + '/scripts/icons/page_white_get.png" title="上线" onclick="App.pushList.status_up('
                    + value + ')"/>';

                } else {
                    b = ca;
                }
                return String.format(b);
            }

            panel.add(this.grid);

        },

        release: function () {
            if (App.pushList.grid.getSelectionModel().hasSelection()) {
                var recs = App.pushList.grid.getSelectionModel()
                    .getSelections();
                var ids = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids = data.id + "," + ids;
                }
                Ext.Msg.confirm('发布', '确定发布', function (btn) {
                    if (btn == 'yes') {
                        Ext.Ajax.request({
                            method: 'post',
                            url: appPath + '/push/release.do',
                            params: {
                                id: ids,
                                status: 0
                            },
                            success: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                    App.pushList.store.load();
                                } else {
                                    Ext.Msg.alert('信息', info);
                                    App.pushList.store.load();
                                }
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
                        });
                    }
                })

            }else{
                Ext.Msg.alert('信息', '请选择要发布的PUSH信息。');
            }
        },
        status_dow: function () {
            if (App.pushList.grid.getSelectionModel().hasSelection()) {
                var recs = App.pushList.grid.getSelectionModel()
                    .getSelections();
                var ids = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids = data.id;
                }
                Ext.Msg.confirm('下线', '确定下线', function (btn) {
                    if (btn == 'yes') {
                        Ext.Ajax.request({
                            method: 'post',
                            url: appPath + '/push/updateStatus.do',
                            params: {
                                id: ids,
                                status: 0
                            },
                            success: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {

                                    App.pushList.store.load();

                                    Ext.Msg.alert('信息', info);

                                } else {
                                    Ext.Msg.alert('信息', info);
                                    App.pushList.store.load();
                                }
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
                        });
                    }
                })

            }
        },
        status_up: function () {
            if (App.pushList.grid.getSelectionModel().hasSelection()) {
                var recs = App.pushList.grid.getSelectionModel()
                    .getSelections();
                var ids = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids = data.id;
                }
                Ext.Msg.confirm('上线', '确定上线', function (btn) {
                    if (btn == 'yes') {
                        Ext.Ajax.request({
                            method: 'post',
                            url: appPath + '/push/updateStatus.do',
                            params: {
                                id: ids,
                                status: 1
                            },
                            success: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    App.pushList.store.load();
                                    Ext.Msg.alert('信息', info);

                                } else {
                                    App.pushList.store.load();
                                    Ext.Msg.alert('信息', info);
                                }
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
                        });
                    }
                })

            }
        },
        add: function () {
            Ext.apply(App.pushList.currentFormValues, {
                id: '0',
                platformId: "",
                targetType: "",
                versions: "",
                title: "",
                content: "",
                url: "",
                status: "",
                publishDate: App.pushList.formatDate(new Date()),
                ditchIds: ""
            });
            App.pushList.dlg.setTitle("增加PUSH信息");
            App.pushList.dlg.show();
        },
        formatDate: function (date) {
            var month = date.getMonth() + 1;
            var monthValue = month > 9 ? month : '0' + month;
            var day = date.getUTCDate();
            var dayValue = day > 9 ? day : '0' + day;
            var hour = date.getHours();
            var hourValue = hour > 9 ? hour : '0' + hour;
            return date.getFullYear() + "-" + monthValue + '-' + dayValue + ' ' + hourValue + ':00:00';
        },

        edit: function () {
            if (App.pushList.grid.getSelectionModel().hasSelection()) {
                App.pushList.dlg.setTitle("编辑PUSH信息");
                var rec = App.pushList.grid.getSelectionModel().getSelected();
                Ext.apply(App.pushList.currentFormValues, {
                    id: rec.data.id,
                    versions: rec.data.versions,
                    platformId: rec.data.platformId,
                    targetType: rec.data.targetType,
                    title: rec.data.title,
                    content: rec.data.content,
                    url: rec.data.url,
                    ditchIds: rec.data.ditchIds,
                    publishDate: rec.data.publishDate,
                    status: rec.data.status
                });

                App.pushList.dlg.show();

            } else {
                Ext.Msg.alert('信息', '请选择要编辑的PUSH信息。');
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

        init: function (params) {
        },

        destroy: function () {
        }
    };
}();
