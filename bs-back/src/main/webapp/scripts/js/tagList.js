App.tagList = function () {
    return {

        currentFormValues: {},

        temporaryValues: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/tag/list.do'
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
                            name: "parentId",
                            type: 'int'
                        },
                        {
                            name: "isLeaf",
                            type: 'int'
                        },
                        {
                            name: "bookCount",
                            type: 'int'
                        },
                        {
                            name: "typeId",
                            type: 'int'
                        },
                        {
                            name: "parentName",
                            allowBlank: false
                        },
                        {
                            name: "createDate",
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
                        id: Ext.getCmp("tagList.id").getValue(),
                        name: Ext.getCmp("tagList.name").getValue(),
                        typeId: Ext.getCmp("tagList.typeId").getValue()
                    }
                );
            });

            return store;
        },

        getParentTagStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1,
                    isLeaf: 0
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/tag/list.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name']
                }),
                listeners: {'load': function () {
                    this.add(zeroRecord);
                }
                }
            });
            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        typeId: App.tagList.temporaryValues.typeId
                    }
                );
            });
            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                fileUpload: false,
                url: appPath + '/tag/save.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 90,
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
                        maxLength: 8,
                        emptyText: '请控制在4个汉字内',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("shortName");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'typeId',
                        fieldLabel: '属性分类',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['类别', '1'],
                                ['性别', '2'],
                                ['内容', '3'],
                                ['补充', '4']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        listeners: {
                            scope: this,
                            select: function(c, r, i) {
                                Ext.apply(App.tagList.temporaryValues, {
                                    typeId: r.data.value
                                });

                                App.tagList.parentTagStore.load();
                                var parentId = App.tagList.frm.form.findField("parentId");
                                parentId.enable();
                                parentId.setValue('');
                            }
                        }
                    }),
                    new Ext.form.ComboBox({
                        name: 'parentId',
                        hiddenName: 'parentId',
                        fieldLabel: '一级分类',
                        store: App.tagList.parentTagStore,
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        anchor: '90%',
                        editable: false,
                        allowBlank: false
                    }),
                    new Ext.form.ComboBox({
                        name: 'isLeaf',
                        hiddenName: 'isLeaf',
                        fieldLabel: '是否有二级分类',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['否', '1'],
                                ['是', '0']
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
                                var typeId = App.tagList.frm.form.findField("typeId").getValue();
                                if (typeId < 3) {
                                    Ext.Msg.alert('提示:', '类别与性别属性分类禁止新增与编辑操作');
                                    return;
                                }
                                var parentId = App.tagList.frm.form.findField("parentId").getValue();
                                var id = App.tagList.frm.form.findField("id").getValue();
                                if (id == parentId) {
                                    Ext.Msg.alert('提示:', '不能选当前标签为一级分类');
                                    return;
                                }
                                var name = this.frm.form.findField("name").getValue();
                                var length = name.replace(/[^x00-xff]/g,"aa").length;
                                if (length > 8) {
                                    Ext.Msg.alert('提示:', "名称包含汉字长度不能超过8个字符");
                                    return;
                                }

                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        var isLeaf = App.tagList.frm.form.findField("isLeaf").getValue();
                                        form.reset();
                                        if (isLeaf == 0) {
                                            App.tagList.parentTagStore.load();
                                        }
                                        App.tagList.store.load();
                                        if (App.categoryList) {
                                            App.categoryList.tagContentStore.load();
                                            App.categoryList.tagSupplyStore.load();
                                        }
                                        if (App.bookList) {
                                            App.bookList.parentTagStore.load();
                                            App.bookList.leafTagStore.load();
                                            App.bookList.tagContentStore.load();
                                            App.bookList.tagSupplyStore.load();
                                        }
                                        App.tagList.dlg.hide();
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
                    '标签ID：',
                    {
                        xtype: 'textfield',
                        id: 'tagList.id',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '标签名称：',
                    {
                        xtype: 'textfield',
                        id: 'tagList.name',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '属性分类：',
                    new Ext.form.ComboBox({
                        id: 'tagList.typeId',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['类别', '1'],
                                ['性别', '2'],
                                ['内容', '3'],
                                ['补充', '4'],
                                ['全部', '']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        width: 75
                    }),
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.tagList.store.load();
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
                        handler: this.add
                    },
                    {xtype : 'tbseparator'},
                    {
                        text: '发布', iconCls: 'icon-database_refresh',
                        handler: function () {
                            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function(btn){
                                if (btn == 'yes') {
                                    var requestConfig = {
                                        url :  appPath+'/tag/publish.do',
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
                    /*
                    ,
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量删除',
                        iconCls: 'icon-page_delete',
                        handler: this.del
                    }
                    */
                ],
                store: this.store,
                sm: sm,
                columns: [sm,
                    {
                        header: "标签ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "标签名称",
                        width: 100,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center'
                    },
                    {
                        header: "属性分类",
                        width: 80,
                        sortable: true,
                        dataIndex: 'typeId',
                        align: 'center',
                        renderer: App.tagList.typeRender
                    },
                    {
                        header: "一级分类",
                        width: 80,
                        sortable: true,
                        dataIndex: 'parentName',
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
                        header: "创建时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'createDate',
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
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.tagList.edit(' + value + ')"/>');
            }

            function typeRender(value, p, record) {
                if (value == 1) {

                }
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.tagList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        changeParent: function () {
            var parentId = App.tagList.frm.form.findField("parentId").getValue();
            if (parentId == 0) {
                App.tagList.frm.getForm().findField('isLeaf').setValue(0);
            } else {
                App.tagList.frm.getForm().findField('isLeaf').setValue(1);
            }
        },

        changeType: function(typeId) {
            Ext.apply(App.tagList.temporaryValues, {
                typeId: typeId
            });

            App.tagList.parentTagStore.load();
            var parentId = App.tagList.frm.form.findField("parentId");
            parentId.enable();
        },


        add: function () {
            Ext.apply(App.tagList.currentFormValues, {
                id: '',
                name: "",
                parentId: "",
                typeId: "",
                isLeaf: ""
            });
            var parentId = App.tagList.frm.form.findField("parentId");
            parentId.disable();
            App.tagList.dlg.setTitle("增加标签");
            App.tagList.dlg.show();
        },

        edit: function () {
            if (App.tagList.grid.getSelectionModel().hasSelection()) {
                App.tagList.dlg.setTitle("编辑标签");
                var rec = App.tagList.grid.getSelectionModel().getSelected();
                App.tagList.changeType(rec.data.typeId);
                Ext.apply(App.tagList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    parentId: rec.data.parentId,
                    isLeaf: rec.data.isLeaf,
                    typeId: rec.data.typeId
                });
                App.tagList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的标签。');
            }
        },

        del: function() {
            if (App.tagList.grid.getSelectionModel().hasSelection()) {
                var recs = App.tagList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                var count = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    words += data.name + '<br>';
                    if (data.bookCount > 0) {
                        count ++;
                    }
                }
                Ext.Msg.confirm('批量删除标签', '已选'+recs.length+'个标签, 其中关联书籍的标签'+count+'个，确认删除？',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/tag/del.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.tagList.store.load();
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
                Ext.Msg.alert('信息', '请选择要删除的标签！');
            }
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.parentTagStore) {
                this.parentTagStore = this.getParentTagStore();
            }
            if (!this.frm) {
                this.frm = this.getForm();
            }; //if(!this.frm)


            if (!this.dlg) {
                this.dlg = this.getDialog();
            }; //if(!this.dlg)
            this.createGrid(id);
        },

        typeRender: function (value, p, record) {
            var name;
            if (value == 0) {
                name = '无';
            } else if (value == 1) {
                name = '类别';
            } else if (value == 2) {
                name = '性别';
            } else if (value == 3) {
                name = '内容';
            } else if (value == 4) {
                name = '补充';
            }
            return String.format(name);
        },

        init: function(params) {},

        destroy: function() {}
    };
}();
