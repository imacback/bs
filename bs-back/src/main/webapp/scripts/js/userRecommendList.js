App.userRecommendList = function () {
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
                    url: appPath + '/userRecommend/list.do'
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
                            name: 'platformId',
                            type: 'int'
                        },
                        {
                            name: 'bookId',
                            allowBlank: false
                        },
                        {
                            name: 'platformName',
                            allowBlank: false
                        }, {
                            name: 'bookName',
                            allowBlank: false
                        }
                        ,
                        {
                            name: 'isUse',
                            type: 'int'

                        }
                    ]
                })
            });
            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        isUse: Ext.getCmp("userRecomme.is_Use").getValue(),
                        bookId: Ext.getCmp("userRecomme.bookId").getValue(),
                        platformId: Ext.getCmp("userRecommendList.platform_id").getValue()
                    }
                );
            });


            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/userRecommend/save.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 130,
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
                        xtype: 'hidden',
                        name: 'bookId',
                        value: ''
                    },
                    {
                        name: 'bookName',
                        hidden: true,
                        id: 'bookName_Re',
                        fieldLabel: '书籍名称',
                        value: '',
                        allowBlank: true
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'platformId',
                        id: 'recommend_platformId',
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
                        mode: 'local',
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {
                        name: 'bookIds',
                        fieldLabel: '书籍编号(多个以;分隔)',
                        maxLength: 100,
                        id: 'bookIds_Re',
                        maxLengthText: "超过最大输入值",
                        regex: /^[0-9;]+$/,
                        regexText: '输入数字;号分隔',
                        allowBlank: false
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'isUse',
                        fieldLabel: '是否可用',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['是', '1'],
                                ['否', '0']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    })


                ],
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
                                        App.userRecommendList.store.reload();
                                        App.userRecommendList.dlg.hide();
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
                width: 430,
                height: 160,
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
                        id: 'userRecommendList.platform_id',
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
                        editable: false,
                        width: 100
                    }),
                    {xtype: 'tbseparator'},
                    '是否可用：',
                    new Ext.form.ComboBox({
                        id: 'userRecomme.is_Use',
                        fieldLabel: '是否可用',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['全部', ''],
                                ['是', '1'],
                                ['否', '0']
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
                    '书籍编号：',
                    {
                        xtype: 'textfield',
                        id: 'userRecomme.bookId',
                        width: 80
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.userRecommendList.store.reload();
                        }
                    }
                ]
            });

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                store: this.store,
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add
                    }
                ],
                sm: sm,
                columns: [{
                    header: "ID",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center'
                }, {
                    header: "书籍编号",
                    width: 80,
                    sortable: true,
                    dataIndex: 'bookId',
                    align: 'center'
                }, {
                    header: "书籍名称",
                    width: 150,
                    sortable: true,
                    dataIndex: 'bookName',
                    align: 'center'
                }, {
                    header: "平台名称",
                    width: 80,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                }, {
                    header: "是否可用",
                    width: 60,
                    sortable: true,
                    dataIndex: 'isUse',
                    renderer: App.yesRender,
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
                var s = record.data.isAct;
                var st = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="image" src="' + appPath + '/scripts/icons/page_go.png" title="发布" onclick="App.userRecommendList.status_up(' + value + ')"/>';
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.userRecommendList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },
        edit: function () {
            if (App.userRecommendList.grid.getSelectionModel().hasSelection()) {
                App.userRecommendList.dlg.setTitle("编辑");
                var rec = App.userRecommendList.grid.getSelectionModel().getSelected();
                Ext.apply(App.userRecommendList.currentFormValues, {
                    id: rec.data.id,
                    bookIds: rec.data.bookId,
                    bookId: rec.data.bookId,
                    isUse: rec.data.isUse,
                    bookName: rec.data.bookName,
                    platformId: rec.data.platformId
                });
                App.userRecommendList.frm.form.findField("bookIds").setReadOnly(true);
                App.userRecommendList.frm.form.findField("platformId").setReadOnly(true);
                App.userRecommendList.dlg.show();

                Ext.getCmp("bookIds_Re").setVisible(false);
                Ext.getCmp("bookName_Re").setVisible(true);

            } else {
                Ext.Msg.alert('信息', '请选择要编辑的信息!。');
            }
        },

        add: function () {
            Ext.apply(App.userRecommendList.currentFormValues, {
                id: '',
                bookIds: '',
                bookId: '',
                bookName: '1',
                isUse: 1,
                platformId: 1
            });
            App.userRecommendList.dlg.setTitle("增加用户推荐书籍");
            App.userRecommendList.frm.form.findField("bookIds").setReadOnly(false);
            App.userRecommendList.frm.form.findField("platformId").setReadOnly(false);
            App.userRecommendList.dlg.show();

            Ext.getCmp("bookIds_Re").setVisible(true);
            Ext.getCmp("bookName_Re").setVisible(false);
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
