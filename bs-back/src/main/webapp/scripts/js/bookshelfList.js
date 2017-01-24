App.bookshelfList = function () {
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
                    url: appPath + '/clientShelf/list.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [{
                        name: 'id',
                        type: 'int'
                    }, {
                        name: 'bookIds',
                        allowBlank: false
                    }, {
                        name: 'chapters',
                        type: 'int'
                    }, {
                        name: 'platformId',
                        type: 'int'
                    }, {
                        name: 'version',
                        allowBlank: false
                    }, {
                        name: 'status',
                        type: 'int'
                    }, {
                        name: "isUseDitch",
                        type: 'int'

                    }, {
                        name: 'ditchIds',
                        allowBlank: false
                    }, {
                        name: "createDate",
                        type: 'date',
                        dateFormat: 'time'
                    }, {
                        name: 'editorName',
                        allowBlank: false
                    }, {
                        name: "platformName",
                        type: 'String'
                    }, {
                        name: "editDate",
                        type: 'date',
                        dateFormat: 'time'
                    }]
                })
            });

            store.on('beforeload', function (thiz, options) {

                var startCreateDate = App.dateFormat(Ext.getCmp("bookshelfList.startCreateDate").getValue());
                var endCreateDate = App.dateFormat(Ext.getCmp("bookshelfList.endCreateDate").getValue());
                if(null!=endCreateDate && ''!=endCreateDate && undefined!=endCreateDate){
                    endCreateDate = endCreateDate.replace("00:00:00","23:59:59");
                }

                Ext.apply(thiz.baseParams, {
                    bookIds: Ext.getCmp("bookshelfList.bookIds").getValue(),
                    platformId: Ext.getCmp("bookshelfList.platformId").getValue(),
                    version: Ext.getCmp("bookshelfList.version").getValue(),
                    ditchIds: Ext.getCmp("bookshelfList.ditch_ids").getValue(),
                    status: Ext.getCmp("bookshelfList.status").getValue(),
                    startCreateDate: startCreateDate,
                    endCreateDate:endCreateDate

                });
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/clientShelf/saveInfo.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 150,
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
                        id: 'ids',
                        value: '0'
                    },
                    {
                        name: 'bookIds',
                        fieldLabel: '书籍编号（以;号分隔）',
                        maxLength: 200,
                        regex: /^[0-9;]+$/,
                        regexText: '只能输入数字以分号分隔',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form
                                        .findField("nickname");
                                    if (obj)
                                        obj.focus();
                                }
                            } // keypress
                        }
                    },

                    new Ext.form.ComboBox({
                        hiddenName: 'chapters',
                        fieldLabel: '预置章节数',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [['1', '1'], ['2', '2'],
                                ['3', '3'], ['4', '4'],
                                ['5', '5'], ['6', '6'],
                                ['7', '7'], ['8', '8'],
                                ['9', '9'], ['10', '10']]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    new Ext.form.ComboBox({
                        hiddenName: 'platformId',
                        id: 'shelf_platformId',
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
                        name: 'version',
                        fieldLabel: '版本号(多版本以;分隔)',
                        maxLength: 190,
                        regex: /^[0-9.;]+$/,
                        regexText: '输入数字、逗号、分号',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form
                                        .findField("version");
                                    if (obj)
                                        obj.focus();
                                }
                            } // keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'is_use_ditch',
                        id: 'isUseDitch',
                        fieldLabel: '是否分渠道',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [['是', '1'], ['否', '0']]
                        }),
                        listeners: {
                            'select': function (combo, record, index) {
                                if (record.data.value == 0) {

                                    Ext.getCmp("ditch_id").setValue("");
                                    App.bookshelfList.frm.form.findField("ditch_ids").setReadOnly(true);
                                } else {
                                    App.bookshelfList.frm.form.findField("ditch_ids").setReadOnly(false);
                                }
                            }
                        },
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {
                        name: 'ditch_ids',
                        id: 'ditch_id',
                        regex: /^[0-9a-zA-Z;.]+$/,
                        regexText: '输入汉字数字或字母',
                        fieldLabel: '渠道号（以;分隔）',
                        maxLength: 190,
                        allowBlank: true,

                        height: 60
//							type:'texTarea'
                    }, new Ext.form.ComboBox({
                        hiddenName: 'status',
                        fieldLabel: '状态',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [['上线', '1'], ['下线', '0']]
                        }),

                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    })],
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-save',
                    scope: this,
                    id: 'userSubmit',
                    handler: function () {
                        if (Ext.getCmp("isUseDitch").getValue() == 1) {
                            if (Ext.getCmp("ditch_id").getValue() == "") {
                                Ext.Msg.alert('信息', "渠道必须填写！");
                                return;
                            }
                        }

                        if (this.frm.form.isValid()) {
                            this.frm.form.submit({
                                success: function (form, action) {
                                    Ext.Msg.alert('提示:', action.result.info);
                                    form.reset();
                                    App.bookshelfList.store.reload();
                                    App.bookshelfList.dlg.hide();
                                },
                                failure: function (form, action) {
                                    Ext.Msg.alert('提示:', action.result.info);
                                },
                                waitMsg: '正在保存数据，稍后...'
                            });
                        }
                    }
                }, {
                    text: '重置',
                    iconCls: 'icon-cross',
                    scope: this,
                    id: 'userSubmit1',
                    handler: function () {
                        this.frm.form.reset();
                        this.frm.form.clearInvalid();
                    }
                }]
                // buttons
            }); // FormPanel

            return frm;
        },

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 650,
                height: 350,
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
            }); // dlg
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
                        id: 'bookshelfList.platformId',
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
                    {
                        xtype: 'tbseparator'
                    },
                    '预置资源：',
                    {
                        xtype: 'textfield',
                        id: 'bookshelfList.bookIds',
                        width: 80
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    '渠道号：',
                    {
                        xtype: 'textfield',
                        id: 'bookshelfList.ditch_ids',
                        width: 80
                    },

                    {
                        xtype: 'tbseparator'
                    },
                    '版本号：',
                    {
                        xtype: 'textfield',
                        id: 'bookshelfList.version',
                        width: 80
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    '创建开始日期：',
                    {
                        id: 'bookshelfList.startCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d',
                        editable:true
                    },
                    {xtype: 'tbseparator'},
                    '创建结束日期：',
                    {
                        id: 'bookshelfList.endCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d',
                        editable:true
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    '状态：',
                    new Ext.form.ComboBox({
                        id: 'bookshelfList.status',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [['全部', ''], ['下线', '0'], ['上线', '1']]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        width: 80
                    }),

                    {
                        xtype: 'tbseparator'
                    }, {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.bookshelfList.store.load();
                        }
                    }]
            });

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [{
                    xtype: 'button',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: this.add
                }],
                store: this.store,
                sm: sm,
                columns: [sm, {
                    header: "序号",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center'
                }, {
                    header: "预置资源",
                    width: 200,
                    sortable: true,
                    dataIndex: 'bookIds',
                    renderer: App.subStr,
                    align: 'center'
                }, {
                    header: "版本号",
                    width: 100,
                    sortable: true,
                    dataIndex: 'version',
                    renderer: App.subStr,
                    align: 'center'
                }, {
                    header: "平台",
                    width: 80,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                }, {
                    header: "渠道",
                    width: 100,
                    sortable: true,
                    dataIndex: 'ditchIds',
                    renderer: App.zeroRenderAll,
                    renderer:App.subStr,
                    align: 'center'
                }, {
                    header: "预置章节数",
                    width: 80,
                    sortable: true,
                    dataIndex: 'chapters',
                    align: 'center'
                }, {
                    header: "是否分渠道",
                    width: 80,
                    dataIndex: 'isUseDitch',
                    renderer: App.yesRender,
                    align: 'center'
                },{
                    header: "创建日期",
                    width: 120,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d h:m:s')
                }, {
                    header: "更新时间",
                    width: 120,
                    sortable: true,
                    dataIndex: 'editDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d h:m:s')
                }, {
                    header: "状态",
                    width: 60,
                    dataIndex: 'status',
                    renderer: App.statusRender,
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
                    + '/scripts/icons/page_edit.png" title="编辑" onclick="App.bookshelfList.edit('
                    + value + ')"/>';
                var b = "";
                if (record.data.status == '1') {
                    b = '&nbsp;<input type="image" src="'
                    + appPath
                    + '/scripts/icons/page_white_put.png" title="下线" onclick="App.bookshelfList.status_dow('
                    + value + ')"/>';
                    ca = b;
                } else {
                    b = '&nbsp;<input type="image" src="'
                    + appPath
                    + '/scripts/icons/page_white_get.png" title="上线" onclick="App.bookshelfList.status_up('
                    + value + ')"/>';
                    ca = ca + b;
                }
                return String.format(ca);
            };

            panel.add(this.grid);
        },

        release: function () {
            if (App.bookshelfList.grid.getSelectionModel().hasSelection()) {
                var recs = App.bookshelfList.grid.getSelectionModel()
                    .getSelections();
                var ids = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids = data.id;
                }
                Ext.Msg.confirm('发布', '确定发布', function (btn) {
                    if (btn == 'yes') {
                        Ext.Ajax.request({
                            method: 'post',
                            url: appPath + '/clientShelf/Release.do',
                            params: {
                                id: ids
                            },
                            success: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                    App.bookshelfList.store.load();
                                } else {
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

        status_dow: function () {
            if (App.bookshelfList.grid.getSelectionModel().hasSelection()) {
                var recs = App.bookshelfList.grid.getSelectionModel()
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
                            url: appPath + '/clientShelf/changStatus.do',
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
                                    App.bookshelfList.store.load();
                                } else {
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

        status_up: function () {
            if (App.bookshelfList.grid.getSelectionModel().hasSelection()) {
                var recs = App.bookshelfList.grid.getSelectionModel()
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
                            url: appPath + '/clientShelf/changStatus.do',
                            params: {
                                id: ids.toString(),
                                status: 1
                            },
                            success: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                    App.bookshelfList.store.load();
                                } else {
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
            Ext.apply(App.bookshelfList.currentFormValues, {
                id: '0',
                bookIds: '',
                chapters: 1,
                platformId: 1,
                version: "",
                is_use_ditch: 0,
                ditch_ids: "",
                status: ""
            });
            App.bookshelfList.dlg.setTitle("增加资源");

            App.bookshelfList.frm.form.findField("ditch_ids").setReadOnly(true);
            App.bookshelfList.frm.form.findField("platformId").setReadOnly(false);
            App.bookshelfList.dlg.show();
        },

        del: function () {
            if (App.bookshelfList.grid.getSelectionModel().hasSelection()) {
                var recs = App.bookshelfList.grid.getSelectionModel()
                    .getSelections();
                var ids = [];
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                }
                Ext.Msg.confirm('删除', '确定', function (btn) {
                    if (btn == 'yes') {
                        Ext.Ajax.request({
                            method: 'post',
                            url: appPath + '/clientShelf/delete.do',
                            params: {
                                id: ids.toString()
                            },
                            success: function (resp, opts) {
                                var result = Ext.util.JSON
                                    .decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                    App.bookshelfList.store.load();
                                } else {
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
                });
            } else {
                Ext.Msg.alert('信息', '请选择要删除的信息！');
            }
        },

        edit: function () {
            if (App.bookshelfList.grid.getSelectionModel().hasSelection()) {
                App.bookshelfList.dlg.setTitle("编辑");
                var rec = App.bookshelfList.grid.getSelectionModel()
                    .getSelected();
                Ext.apply(App.bookshelfList.currentFormValues, {
                    id: rec.data.id,
                    bookIds: rec.data.bookIds,
                    chapters: rec.data.chapters,
                    platformId: rec.data.platformId,
                    version: rec.data.version,
                    is_use_ditch: rec.data.isUseDitch,
                    ditch_ids: rec.data.ditchIds,
                    status: rec.data.status
                });

                App.bookshelfList.dlg.show();
                App.bookshelfList.frm.form.findField("ditch_ids").setReadOnly(false);
                App.bookshelfList.frm.form.findField("platformId").setReadOnly(true);
                if (rec.data.isUseDitch == 0) {
                    Ext.getCmp("ditch_id").setValue("");
                    App.bookshelfList.frm.form.findField("ditch_ids").setReadOnly(true);
                }
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的信息。');
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
            ; // if(!this.frm)

            if (!this.dlg) {
                this.dlg = this.getDialog();
            }
            ; // if(!this.dlg)
            this.createGrid(id);

        },

        init: function (params) {
        },

        destroy: function () {
        }
    };
}();
