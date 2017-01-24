App.categoryList = function () {
    return {

        currentFormValues: {},

        temporaryValues: {
            tagCount:0,
            tagContentCount:0,
            tagSupplyCount:0
        },

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/category/list.do'
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
                            name: "recommend",
                            allowBlank: true
                        },
                        {
                            name: "logo",
                            allowBlank: true
                        },
                        {
                            name: "bookCount",
                            type: 'int'
                        },
                        {
                            name: "parentName",
                            allowBlank: false
                        },
                        {
                            name: "orderId",
                            type: 'int'
                        },
                        {
                            name: "bookCount",
                            type: 'int'
                        },
                        {
                            name: "tagClassifyId",
                            type: 'int'
                        },
                        {
                            name: "tagClassifyName",
                            allowBlank: false
                        },
                        {
                            name: "tagSexId",
                            type: 'int'
                        },
                        {
                            name: "tagSexName",
                            allowBlank: false
                        },
                        {
                            name: "tagContentIds",
                            allowBlank: false
                        },
                        {
                            name: "tagContentNames",
                            allowBlank: false
                        },
                        {
                            name: "tagSupplyIds",
                            allowBlank: false
                        },
                        {
                            name: "distributeIds",
                            allowBlank: false
                        },
                        {
                            name: "tagSupplyNames",
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
                        id: Ext.getCmp("categoryList.id").getValue(),
                        name: Ext.getCmp("categoryList.name").getValue(),
                        parentId: Ext.getCmp("categoryList.parentId").getValue(),
                        isLeaf:1
                    }
                );
            });

            return store;
        },
        getDistributeStore: function () {
            var distributeStore = new Ext.data.Store({
                baseParams: {
                    status:1,
                    name: "",
                    isCategory:1
                },
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/distribute/list.do'
                }),
               autoLoad: true,
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
                            name: "name",
                            allowBlank: false
                        }
                    ]
                })
            });
             return distributeStore;
        },
        getCategorySearchStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1,
                    isLeaf: 0
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/category/list.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name']
                }),
                listeners: {'load': function () {
                    this.add(allRecord);
                }
                }
            });
            return store;
        },

        getParentCategoryStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1,
                    isLeaf: 0
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/category/list.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name']
                })
            });
            return store;
        },

        getTagContentStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1,
                    isLeaf: 0,
                    typeId: 3
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/tag/subList.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name', 'children', 'scope']
                })
            });

            return store;
        },

        getTagSupplyStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1,
                    isLeaf: 0,
                    typeId: 4
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/tag/subList.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name', 'children', 'scope']
                })
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                fileUpload: true,
                method: 'POST',
                url: appPath + '/category/save.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                autoScroll : true,
                bodyStyle: 'padding:5px',
                frame: true,
                items: [
                    {
                        xtype: 'fieldset',
                        title: '基本信息',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank: false,
                            anchor: '95%',
                            enableKeyEvents: true
                        },
                        items: [
                            {
                                name: 'id',
                                fieldLabel: '分类id',
                                readOnly: true,
                                allowBlank: true
                            },
                            {
                                name: 'name',
                                fieldLabel: '名称',
                                maxlength: 25,
                                regex: /^[0-9a-zA-Z\u4E00-\u9FA5]+$/,
                                regexText: '只能输入25个数字英文及汉字',
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("parentId");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            new Ext.form.ComboBox({
                                name: 'parentId',
                                hiddenName: 'parentId',
                                fieldLabel: '一级分类',
                                store: App.categoryList.parentCategoryStore,
                                displayField: 'name',
                                valueField: 'id',
                                triggerAction: 'all',
                                anchor: '90%',
                                editable: false,
                                allowBlank: false
                            }),
                            {
                                name: 'orderId',
                                fieldLabel: '分类排序',
                                maxlength: 5,
                                regex:/^\d*$/,
                                regexText:'请输入大于0整数',
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("recommend");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            {
                                name: 'recommend',
                                fieldLabel: '推荐语',
                                maxLength: 28,
                                emptyText: '请控制在14字内',
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            var obj = this.frm.form.findField("file");
                                            if (obj) obj.focus();
                                        }
                                    } //keypress
                                }
                            },
                            {
                                xtype: 'fileuploadfield',
                                fieldLabel: '图标',
                                name: 'file',
                                id: 'categoryList.pic',
                                width: 400,
                                emptyText: '选择图片文件...',
                                regex: /^[^\u4e00-\u9fa5]*$/,
                                regexText: '文件名中不能包含中文',
                                buttonText: '',
                                buttonCfg: {
                                    iconCls: 'icon-search'
                                },
                                allowBlank: true,
                                listeners:{
                                    'fileselected': function(){}
                                }
                            },
                            {
                                xtype: 'fieldset',
                                id: 'categoryList.operateDistributeFormFieldSet',
                                fieldLabel: '推广渠道',
                                labelWidth: 60,
                                collapsed: false,
                                collapsible: false,
                                autoHeight: true,
                                layout: 'column',
                                style: 'border:0px;padding:0px;margin:0px;padding-top:0px;padding-left:2px;',
                                defaults: {
                                    border: false,
                                    anchor: '90%'
                                },
                                items: []
                            }
                        ]
                    },

                    {
                        xtype: 'fieldset',
                        title: '标签信息',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank: true,
                            anchor: '95%',
                            enableKeyEvents: true,
                            readonly: true,
                            listeners: {
                                scope: this,
                                keypress: App.categoryList.editTag,
                                focus: App.categoryList.editTag
                            }
                        },
                        items: [
                            {
                                xtype: 'hidden',
                                id: 'categoryList.tagClassifyId',
                                name: 'tagClassifyId',
                                value: ''
                            },
                            {
                                name: 'tagClassifyName',
                                id: 'categoryList.tagClassifyName',
                                fieldLabel: '类别属性',
                                maxlength: 32,
                                readOnly: true
                            },
                            {
                                xtype: 'hidden',
                                id: 'categoryList.tagSexId',
                                name: 'tagSexId',
                                value: ''
                            },
                            {
                                name: 'tagSexName',
                                id: 'categoryList.tagSexName',
                                fieldLabel: '性别属性',
                                maxlength: 32,
                                readOnly: true
                            },
                            {
                                xtype: 'textarea',
                                name: 'tagContentNames',
                                id: 'categoryList.tagContentNames',
                                fieldLabel: '内容属性',
                                maxLength: 200,
                                height: 40,
                                readOnly: true
                            },
                            {
                                xtype: 'hidden',
                                id: 'categoryList.tagContentIds',
                                name: 'tagContentIds',
                                value: ''
                            },
                            {
                                xtype: 'hidden',
                                id: 'categoryList.tagSupplyIds',
                                name: 'tagSupplyIds',
                                value: ''
                            },
                            {
                                xtype: 'textarea',
                                name: 'tagSupplyNames',
                                id: 'categoryList.tagSupplyNames',
                                fieldLabel: '补充属性',
                                maxLength: 200,
                                height: 40,
                                readOnly: true
                            }
                        ]
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
                                var recommend = this.frm.form.findField("recommend").getValue();
                                var length = recommend.replace(/[^x00-xff]/g,"aa").length;
                                if (length > 28) {
                                    Ext.Msg.alert('提示:', "推荐语包含汉字长度不能超过28个字符");
                                    return;
                                }
                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.categoryList.store.load();
                                        App.categoryList.dlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', action.failureType);
                                        form.reset();
                                        App.categoryList.store.load();
                                        App.categoryList.dlg.hide();
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

        getTagForm: function () {
            var tfrm = new Ext.form.FormPanel({
                fileUpload: false,
                url: '',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:10px',
                autoScroll: true,
                frame: true,
                items: [
                    {
                        xtype: 'fieldset',
                        title: '类别属性',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank: false,
                            anchor: '95%',
                            enableKeyEvents: true
                        },
                        items: [
                            {
                                xtype: 'radiogroup',
                                name: 'tagClassifyId',
                                allowBlank: false,
                                anchor: '95%',
                                items: [{
                                    columnWidth: '.5',
                                    items: [
                                        {xtype: 'label', text: '出版', cls:'x-form-check-group-label', anchor:'-15'},
                                        {boxLabel: '非小说', name: 'tagClassifyIdItem', alias:'出版-非小说', inputValue: 102},
                                        {boxLabel: '小说', name: 'tagClassifyIdItem', alias:'出版-小说', inputValue: 101}
                                    ]
                                },{
                                    columnWidth: '.5',
                                    items: [
                                        {xtype: 'label', text: '原创', cls:'x-form-check-group-label', anchor:'-15'},
                                        {boxLabel: '非小说', name: 'tagClassifyIdItem', alias:'原创-非小说', inputValue: 104},
                                        {boxLabel: '小说', name: 'tagClassifyIdItem', alias:'原创-小说', inputValue: 103}
                                    ]
                                }],
                                listeners: {
                                    'change': function(group,checked) {
                                        var field = Ext.getCmp('categoryList.tagClassifyName');
                                        field.setValue(checked.alias);
                                        field = Ext.getCmp('categoryList.tagClassifyId');
                                        field.setValue(checked.inputValue);
                                    }
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'fieldset',
                        title: '性别属性',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        defaults: {
                            allowBlank: false,
                            anchor: '95%',
                            enableKeyEvents: true
                        },
                        items: [
                            {
                                xtype: 'radiogroup',
                                name: 'tagSexId',
                                allowBlank: false,
                                columns: 3,
                                vertical: true,
                                items: [
                                    {boxLabel: '男频', name: 'tagSexIdItem', inputValue: 105},
                                    {boxLabel: '女频', name: 'tagSexIdItem', inputValue: 106},
                                    {boxLabel: '综合', name: 'tagSexIdItem', inputValue: 107}
                                ],
                                listeners: {
                                    'change': function(group,checked) {
                                        var field = Ext.getCmp('categoryList.tagSexName');
                                        field.setValue(checked.boxLabel);
                                        field = Ext.getCmp('categoryList.tagSexId');
                                        field.setValue(checked.inputValue);
                                    }
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'fieldset',
                        id: 'categoryList.tagContentFieldSet',
                        title: '内容属性',
                        labelWidth: 80,
                        layout: 'form',
                        collapsed: false,
                        collapsible: false,
                        items: []
                    },
                    {
                        xtype: 'fieldset',
                        id: 'categoryList.tagSupplyFieldSet',
                        title: '补充属性',
                        labelWidth: 80,
                        layout: 'form',
                        collapsed: false,
                        collapsible: false,
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
                            if (this.tfrm.form.isValid()) {
                                if (App.categoryList.tagSubmitCheck('categoryList.tagNames', App.categoryList.tagContentStore, 3) &&
                                    App.categoryList.tagSubmitCheck('categoryList.tagNames', App.categoryList.tagSupplyStore, 4)) {
                                    App.categoryList.tdlg.hide();
                                }
                            }
                        }
                    }
                ] //buttons
            }); //FormPanel

            return tfrm;
        },

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 620,
                height: 460,
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

        getTagDialog: function () {
            var tdlg = new Ext.Window({
                width: 500,
                height: 450,
                title: '',
                plain: true,
                closable: true,
                resizable: true,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.tfrm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.tfrm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.tfrm.form.clearInvalid();
                    }
                }
            }); //dlg
            return tdlg;
        },

        createGrid: function (id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    '分类ID：',
                    {
                        xtype: 'textfield',
                        id: 'categoryList.id',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '分类名称：',
                    {
                        xtype: 'textfield',
                        id: 'categoryList.name',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '一级分类：',
                    new Ext.form.ComboBox({
                        id: "categoryList.parentId",
                        store: this.categorySearchStore,
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false,
                        width: 100
                    }),
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.categoryList.store.load();
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
                                        url :  appPath+'/category/publish.do',
                                        callback:function(o, s, r) {
                                            var result = Ext.util.JSON.decode(r.responseText);
                                            Ext.Msg.alert('提示',result.info);
                                        }
                                    }
                                    Ext.Ajax.request(requestConfig);
                                }
                            });
                        }
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量删除',
                        iconCls: 'icon-page_delete',
                        handler: this.del
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [sm,
                    {
                        header: "分类ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "分类名称",
                        width: 120,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center'
                    },
                    {
                        header: "推荐语",
                        width: 200,
                        sortable: true,
                        dataIndex: 'recommend',
                        align: 'center'
                    },
                    {
                        header: "一级分类",
                        width: 80,
                        sortable: true,
                        dataIndex: 'parentName',
                        align: 'center'
                    },
                    {
                        header: "图标",
                        width: 50,
                        cls: 'xx-icon',
                        sortable: false,
                        dataIndex: 'logo',
                        align:'center',
                        renderer:this.imageViewRender
                    },
                    {
                        header: "排序号",
                        width: 60,
                        sortable: true,
                        dataIndex: 'orderId',
                        align: 'center',
                        editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            regex:/^\d*$/,
                            regexText:'请输入大于0整数'
                        }
                    },
                    {
                        header: "书籍数量",
                        width: 80,
                        sortable: true,
                        dataIndex: 'bookCount',
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
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.categoryList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        changeParent: function () {
            var parentId = App.categoryList.frm.form.findField("parentId").getValue();
            if (parentId == 0) {
                App.categoryList.frm.getForm().findField('isLeaf').setValue(0);
            } else {
                App.categoryList.frm.getForm().findField('isLeaf').setValue(1);
            }
        },

        add: function () {
            App.categoryList.temporaryValues.tagCount = 0;
            App.categoryList.temporaryValues.tagContentCount = 0;
            App.categoryList.buildCheckGroup('', 'categoryList.tagContentFieldSet', 'categoryList.tagNames',
                'categoryList.tagContentIds', 'categoryList.tagContentNames',
                App.categoryList.tagContentStore, 3);
            App.categoryList.temporaryValues.tagContentCount = App.categoryList.temporaryValues.tagCount;

            App.categoryList.temporaryValues.tagCount = 0;
            App.categoryList.temporaryValues.tagSupplyCount = 0;
            App.categoryList.buildCheckGroup('', 'categoryList.tagSupplyFieldSet', 'categoryList.tagNames',
                'categoryList.tagSupplyIds', 'categoryList.tagSupplyNames',
                App.categoryList.tagSupplyStore, 4);
            App.categoryList.temporaryValues.tagSupplyCount = App.categoryList.temporaryValues.tagCount;
            App.categoryList.buildSimpleCheckGroup("","categoryList.operateDistributeFormFieldSet","distributeIds");

           // categoryList.operateDistributeFormFieldSet

            Ext.apply(App.categoryList.currentFormValues, {
                id: '',
                name: "",
                parentId: "",
                isLeaf: "",
                orderId: "",
                recommend: "",
                tagClassifyId: "",
                tagClassifyName: "",
                tagSexId: "",
                tagSexName: "",
                tagContentIds: "",
                tagContentNames: "",
                tagSupplyIds: "",
                distributeIds:"",
                tagSupplyNames: ""
            });
            App.categoryList.frm.form.findField("id").disable();
            App.categoryList.dlg.setTitle("增加分类");
            App.categoryList.dlg.show();
        },

        editTag: function () {
            var tagClassifyId = App.categoryList.frm.form.findField("tagClassifyId").getValue();
            var tagSexId = App.categoryList.frm.form.findField("tagSexId").getValue();

            App.categoryList.tfrm.form.findField("tagClassifyId").setValue(tagClassifyId);
            App.categoryList.tfrm.form.findField("tagSexId").setValue(tagSexId);

            App.categoryList.tdlg.setTitle("编辑标签");
            App.categoryList.tdlg.show();
        },

        edit: function () {
            if (App.categoryList.grid.getSelectionModel().hasSelection()) {
                App.categoryList.dlg.setTitle("编辑分类");
                var rec = App.categoryList.grid.getSelectionModel().getSelected();
                App.categoryList.frm.form.findField("id").enable();
                Ext.apply(App.categoryList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    parentId: rec.data.parentId,
                    orderId: rec.data.orderId,
                    recommend: rec.data.recommend,
                    tagClassifyId: rec.data.tagClassifyId,
                    tagClassifyName: rec.data.tagClassifyName,
                    tagSexId: rec.data.tagSexId,
                    tagSexName: rec.data.tagSexName,
                    tagContentIds: rec.data.tagContentIds,
                    tagContentNames: rec.data.tagContentNames,
                    distributeIds:rec.data.distributeIds,
                    tagSupplyIds: rec.data.tagSupplyIds,
                    tagSupplyNames: rec.data.tagSupplyNames
                });

                App.categoryList.temporaryValues.tagCount = 0;
                App.categoryList.temporaryValues.tagContentCount = 0;
                App.categoryList.buildCheckGroup(rec.data.tagContentIds, 'categoryList.tagContentFieldSet', 'categoryList.tagNames',
                    'categoryList.tagContentIds', 'categoryList.tagContentNames',
                    App.categoryList.tagContentStore, 3);
                App.categoryList.temporaryValues.tagContentCount = App.categoryList.temporaryValues.tagCount;

                App.categoryList.temporaryValues.tagCount = 0;
                App.categoryList.temporaryValues.tagSupplyCount = 0;
                App.categoryList.buildCheckGroup(rec.data.tagSupplyIds, 'categoryList.tagSupplyFieldSet', 'categoryList.tagNames',
                    'categoryList.tagSupplyIds', 'categoryList.tagSupplyNames',
                    App.categoryList.tagSupplyStore, 4);
                App.categoryList.temporaryValues.tagSupplyCount = App.categoryList.temporaryValues.tagCount;
                App.categoryList.buildSimpleCheckGroup(rec.data.distributeIds,"categoryList.operateDistributeFormFieldSet","distributeIds");
                App.categoryList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的分类。');
            }
        },

        del: function() {
            if (App.categoryList.grid.getSelectionModel().hasSelection()) {
                var recs = App.categoryList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    words += data.name + '<br>';
                }
                Ext.Msg.confirm('批量删除分类', '确定删除所选的'+recs.length+'个分类？',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/category/del.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.categoryList.store.load();
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
                Ext.Msg.alert('信息', '请选择要删除的分类！');
            }
        },


        buildSimpleCheckGroup: function (values, fieldSetId, name) {

            var length =0;
            if (values && values.length >0) {
                 length = values.length;
            }


            var size = this.distributeStore.getTotalCount();
            var fieldSet = Ext.getCmp(fieldSetId);
            fieldSet.removeAll();

            for (var i = -1; i < size; i++) {
                var moduleId = '';
                var moduleName = '';
                var checkboxId = '';
                var checked = false;
                var hidden = true;
                if (i > -1) {
                    hidden = false;
                    checkboxId = name + '_' + i;
                    moduleId = this.distributeStore.getAt(i).data.id;
                    moduleName = this.distributeStore.getAt(i).data.name;
                    if (values != '') {
                        for (var j = 0; j < length; j++) {
                            if (values[j] == moduleId) {
                                checked = true;
                            }
                        }
                    } else {
                        checked = false;
                    }
                }

                var checkboxModule = new Ext.form.Checkbox({
                    checked: checked,
                    width: 90,
                    id: "boxModule_" + checkboxId,
                    name: name,
                    boxLabel: moduleName,
                    labelSeparator: "",  // 当没有标题时，不要 “:” 号，不要标题分隔
                    inputValue: moduleId,
                    hidden: hidden
                });

                fieldSet.add(checkboxModule); // 这里我是一个 fieldset ，你也可以是一个 form 或者别的东西。
            }
        },


        buildCheckGroup: function (values, fieldSetId, checkBoxName, formFieldId, showFieldId, store, typeId) {
            //var ids = values.split(',');
            var ids = values;
            var size = store.getTotalCount();
            var fieldSet = Ext.getCmp(fieldSetId);
            fieldSet.removeAll();
            var index = 0;
            for (var i = 0; i < size; i++) {
                var record = store.getAt(i).data;
                var id = record.id;
                var name = record.name;
                var children = record.children;

                var totalItems = children.totalItems;
                var checked = false;
                var itemCls = '';
                if ((i+1)%2 == 0) {
                    itemCls = 'x-check-group-alt';
                }

                var subSet = new Ext.form.FieldSet({
                    fieldLabel: name,
                    border: false,
                    defaultType: 'checkbox',
                    layout: 'column',
                    itemCls: itemCls,
                    defaults: {
                        layout: 'form',
                        autoHeight: true,
                        columnWidth: .25
                    },
                    items:[]
                });

                for (var k = 0; k < totalItems; k++) {
                    var moduleId = children.items[k].id;
                    var moduleName = children.items[k].name;
                    var idPrefix = checkBoxName + '_' + typeId + '_';

                    checked = false;
                    for (var j = 0, length = ids.length; j < length; j++) {
                        if (ids[j] == moduleId) {
                            checked = true;
                            break;
                        }
                    }

                    var checkbox = new Ext.form.Checkbox({
                        checked: checked,
                        width: 90,
                        id: idPrefix + index,
                        typeId: typeId,
                        idPrefix: idPrefix,
                        formFieldId: formFieldId,
                        showFieldId: showFieldId,
                        name: checkBoxName,
                        totalItems: totalItems,
                        boxLabel: moduleName,
                        labelSeparator: "",  // 当没有标题时，不要 “:” 号，不要标题分隔
                        inputValue: moduleId,
                        listeners: {
                            'check': App.categoryList.tagChecked
                        }
                    });

                    subSet.add(checkbox);
                    index ++;
                }

                fieldSet.add(subSet);
            }

            App.categoryList.temporaryValues.tagCount = index;
        },

        tagChecked: function(obj, check) {
            var name = '';
            var value = '';
            var count = 0;
            if (obj.typeId == 3) {
                count = App.categoryList.temporaryValues.tagContentCount;
            } else if (obj.typeId == 4) {
                count = App.categoryList.temporaryValues.tagSupplyCount;
            }
            for (var m=0,n=0;m<count;m++) {
                var box = Ext.getCmp(obj.idPrefix+m);
                if (box.checked) {
                    if (n > 0) {
                        name = name + ',';
                        value = value + ','
                    }
                    name = name + box.boxLabel;
                    value = value + box.inputValue;
                    n++;
                }
            }
            var field = Ext.getCmp(obj.showFieldId);
            field.setValue(name);
            field = Ext.getCmp(obj.formFieldId);
            field.setValue(value);
        },

        tagSubmitCheck: function(checkBoxName, store, typeId) {
            var size = store.getTotalCount();
            var index = 0;
            for (var i = 0; i < size; i++) {
                var record = store.getAt(i).data;
                var id = record.id;
                var name = record.name;
                var children = record.children;
                var scope = record.scope;
                var totalItems = children.totalItems;
                var checked = false;

                for (var k = 0; k < totalItems; k++) {
                    var boxId = checkBoxName + '_' + typeId + '_' + index;
                    var box = Ext.getCmp(boxId);
                    if (box.getValue()) {
                        checked = true;
                    }
                    index ++;
                }

                if (!checked) {
                    if (scope != '') {
                        var data = scope.split(',');
                        if (parseInt(data[0]) > 0) {
                            Ext.Msg.alert('提示', '['+name+'] 最少选择一项！');
                            return false;
                        }
                    }
                }
            }

            return true;
        },

        imageViewRender: function (value,p,record) {
            if (!value) return "";
            return String.format('<img style="max-width:30px;" src="' + value + '" alt="' + value + '" />');
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.distributeStore) {
                 this.distributeStore = this.getDistributeStore();
            }
            if (!this.categorySearchStore) {
                this.categorySearchStore = this.getCategorySearchStore();
            }
            if (!this.parentCategoryStore) {
                this.parentCategoryStore = this.getParentCategoryStore();
            }
            if (!this.tagContentStore) {
                this.tagContentStore = this.getTagContentStore();
            }
            if (!this.tagSupplyStore) {
                this.tagSupplyStore = this.getTagSupplyStore();
            }
            if (!this.frm) {
                this.frm = this.getForm();
            }; //if(!this.frm)

            if (!this.dlg) {
                this.dlg = this.getDialog();
            }; //if(!this.dlg)

            if (!this.tfrm) {
                this.tfrm = this.getTagForm();
            }; //if(!this.frm)
            if (!this.tdlg) {
                this.tdlg = this.getTagDialog();
            }; //if(!this.dlg)

            this.createGrid(id);
        },

        init: function(params) {
        this.distributeStore.load();
        },

        destroy: function() {}
    };
}();
