App.bookList = function () {
    return {

        currentFormValues: {},

        temporaryValues: {
            tagCount: 0,
            tagContentCount: 0,
            tagSupplyCount: 0,
            formTagSexId:'',
            formTagSexName:'',
            formTagClassifyId:'',
            formTagClassifyName:'',
            formTagStoryId:'',
            formTagStoryName:'',
            formTagContentIds:'',
            formTagContentNames:'',
            formTagSupplyIds:'',
            formTagSupplyNames:''
        },

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: 50
                },
                autoLoad: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/book/list.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'id',
                            type: 'long'
                        },
                        {
                            name: 'name',
                            allowBlank: false
                        },
                        {
                            name: 'cpBookId',
                            allowBlank: false
                        },
                        {
                            name: 'author',
                            allowBlank: false
                        },
                        {
                            name: 'tagClassifyId',
                            type: 'int'
                        },
                        {
                            name: 'tagClassifyName',
                            allowBlank: false
                        },
                        {
                            name: 'tagSexId',
                            type: 'int'
                        },
                        {
                            name: 'tagSexName',
                            allowBlank: false
                        },
                        {
                            name: 'tagStoryName',
                            allowBlank: false
                        },
                        {
                            name: "words",
                            type: 'int'
                        },
                        {
                            name: "isSerial",
                            type: 'int'
                        },
                        {
                            name: "chapters",
                            type: 'int'
                        },
                        {
                            name: "isFee",
                            type: 'int'
                        },
                        {
                            name: "publishChapters",
                            type: 'int'
                        },
                        {
                            name: "updateChapterDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "editDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "status",
                            type: 'int'
                        },
                        {
                            name: 'providerId',
                            type: 'int'
                        },
                        {
                            name: 'providerName',
                            allowBlank: false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                var startEditDate = App.dateFormat(Ext.getCmp('bookList.search.startEditDate').getValue());
                var endEditDate = App.dateFormat(Ext.getCmp('bookList.search.endEditDate').getValue());
                Ext.apply(
                    thiz.baseParams,
                    {
                        id: Ext.getCmp("bookList.search.id").getValue(),
                        name: Ext.getCmp("bookList.search.name").getValue(),
                        author: Ext.getCmp("bookList.search.author").getValue(),
                        tagClassifyId: Ext.getCmp("bookList.search.tagClassifyId").getValue(),
                        tagSexId: Ext.getCmp("bookList.search.tagSexId").getValue(),
                        tagStoryId: Ext.getCmp("bookList.search.tagStoryId").getValue(),
                        providerId: Ext.getCmp("bookList.search.providerId").getValue(),
                        isSerial: Ext.getCmp("bookList.search.isSerial").getValue(),
                        isFee: Ext.getCmp("bookList.search.isFee").getValue(),
                        status: Ext.getCmp("bookList.search.status").getValue(),
                        categoryId: Ext.getCmp("bookList.search.categoryId").getValue(),
                        startEditDate: startEditDate,
                        endEditDate: endEditDate,
                        operatePlatformId: Ext.getCmp("bookList.search.operatePlatformId").getValue()
                    }
                );
            });

            return store;
        },

        getParentTagStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1
                },
                autoLoad: true,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/tag/parentList.do'
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
                            name: "name",
                            allowBlank: false
                        },
                        {
                            name: "scope",
                            allowBlank: false
                        }
                    ]
                })
            });

            return store;
        },

        getLeafTagStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isLeaf: 1
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
                    fields: ['id', 'name', 'parentId']
                })
            });

            return store;
        },

        getPlatformStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/platform/list.do'
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
                            name: "name",
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

        getProviderStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/provider/list.do'
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

        getBatchOptionStore: function () {
            var store = new Ext.data.Store({
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/batch/list.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'contractId']
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
                url: appPath + '/book/save.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:0px',
                layout: 'form',
                autoScroll: true,
                frame: true,
                items: [
                    {
                        layout:'form',
                        xtype: 'fieldset',
                        title: '基本信息',
                        defaults: {
                            layout: 'form',
                            border: false,
                            allowBlank: false,
                            enableKeyEvents: true
                        },
                        items: [
                            {
                                layout:'column',
                                xtype: 'fieldset',
                                baseCls: 'xx-fieldset',
                                defaults: {
                                    layout: 'form',
                                    border: false,
                                    allowBlank: false,
                                    enableKeyEvents: true
                                },
                                items: [
                                    {
                                        columnWidth: 0.5,
                                        layout:'form',
                                        xtype: 'fieldset',
                                        labelWidth: 60,
                                        defaultType: 'textfield',
                                        defaults: {
                                            layout: 'form',
                                            border: false,
                                            allowBlank: false,
                                            enableKeyEvents: true,
                                            anchor: '-25' // leave room for error icon
                                        },
                                        items: [
                                            {
                                                name: 'id',
                                                fieldLabel: '书ID',
                                                allowBlank: true,
                                                readOnly: true
                                            },
                                            {
                                                name: 'cpBookId',
                                                fieldLabel: 'cp书籍ID',
                                                maxLength: 32,
                                                maxLengthText: '最大长度为32字符',
                                                allowBlank: true,
                                                readOnly: true
                                            },
                                            {
                                                name: 'name',
                                                fieldLabel: '书名',
                                                allowBlank: false,
                                                maxLength: 50,
                                                maxLengthText: '最大长度为32字符',
                                                listeners: {
                                                    scope: this,
                                                    keypress: function (field, e) {
                                                        if (e.getKey() == 13) {
                                                            var obj = this.frm.form.findField("author");
                                                            if (obj) obj.focus();
                                                        }
                                                    } //keypress
                                                }
                                            },
                                            {
                                                name: 'author',
                                                fieldLabel: '作者',
                                                allowBlank: false,
                                                maxLength: 32,
                                                listeners: {
                                                    scope: this,
                                                    keypress: function (field, e) {
                                                        if (e.getKey() == 13) {
                                                            var obj = this.frm.form.findField("tagStoryName");
                                                            if (obj) obj.focus();
                                                        }
                                                    } //keypress
                                                }
                                            },
                                            {
                                                xtype: 'hidden',
                                                id: 'bookList.tagStoryId',
                                                name: 'tagStoryId',
                                                value: ''
                                            },
                                            {
                                                name: 'tagStoryName',
                                                id: 'bookList.tagStoryName',
                                                fieldLabel: '故事类型',
                                                allowBlank: false,
                                                readOnly: true,
                                                listeners: {
                                                    scope: this,
                                                    keypress: App.bookList.editTag,
                                                    focus: App.bookList.editTag
                                                }
                                            },
                                            {
                                                name: 'words',
                                                fieldLabel: '字数',
                                                allowBlank: true,
                                                maxLength: 11,
                                                disabled: true,
                                                readOnly: true,
                                                listeners: {
                                                    scope: this,
                                                    keypress: function (field, e) {
                                                        if (e.getKey() == 13) {
                                                            var obj = this.frm.form.findField("isSerial");
                                                            if (obj) obj.focus();
                                                        }
                                                    } //keypress
                                                }
                                            },
                                            new Ext.form.ComboBox({
                                                hiddenName: 'isSerial',
                                                fieldLabel: '是否连载',
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
                                                editable: false,
                                                listeners: {
                                                    "select": App.bookList.changeIsSerial
                                                }
                                            }),
                                            new Ext.form.ComboBox({
                                                hiddenName: 'checkLevel',
                                                fieldLabel: '审核等级',
                                                store: new Ext.data.SimpleStore({
                                                    fields: ['text', 'value'],
                                                    data: [
                                                        ['10', '10'],
                                                        ['20', '20'],
                                                        ['30', '30']
                                                    ]
                                                }),
                                                mode: 'local',
                                                displayField: 'text',
                                                valueField: 'value',
                                                triggerAction: 'all',
                                                editable: false
                                            }),
                                            {
                                                xtype: 'hidden',
                                                id: 'bookList.tagClassifyId',
                                                name: 'tagClassifyId',
                                                value: ''
                                            },
                                            {
                                                name: 'tagClassifyName',
                                                id: 'bookList.tagClassifyName',
                                                fieldLabel: '类别属性',
                                                allowBlank: false,
                                                readOnly: true,
                                                listeners: {
                                                    scope: this,
                                                    keypress: App.bookList.editTag,
                                                    focus: App.bookList.editTag
                                                }
                                            },
                                            {
                                                xtype: 'hidden',
                                                id: 'bookList.tagSexId',
                                                name: 'tagSexId',
                                                value: ''
                                            },
                                            {
                                                name: 'tagSexName',
                                                id: 'bookList.tagSexName',
                                                fieldLabel: '性别属性',
                                                allowBlank: false,
                                                readOnly: true,
                                                listeners: {
                                                    scope: this,
                                                    keypress: App.bookList.editTag,
                                                    focus: App.bookList.editTag
                                                }
                                            }
                                        ]
                                    },
                                    {
                                        columnWidth: 0.5,
                                        layout:'form',
                                        xtype: 'fieldset',
                                        labelWidth: 60,
                                        defaultType: 'textfield',
                                        defaults: {
                                            layout: 'form',
                                            border: false,
                                            allowBlank: false,
                                            enableKeyEvents: true,
                                            anchor: '-25' // leave room for error icon
                                        },
                                        items: [
                                            {
                                                name: 'onlineDate',
                                                fieldLabel: '上线时间',
                                                xtype: "datefield",
                                                format : 'Y-m-d',
                                                editable: false,
                                                allowBlank: true,
                                                readOnly: true,
                                                disabled: true
                                            },
                                            {
                                                xtype: 'fileuploadfield',
                                                fieldLabel: '封面',
                                                name: 'file',
                                                id: 'bookList.pic',
                                                width: 400,
                                                emptyText: '选择图片文件...',
                                                regex: /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/,
                                                regexText: '请上传jpg、jpeg、gif、png或bmp格式图片文件',
                                                buttonText: '',
                                                buttonCfg: {
                                                    iconCls: 'icon-search'
                                                },
                                                allowBlank: true,
                                                listeners:{
                                                    fileselected: function(obj, v){
                                                        var fileSize = 0;
                                                        if (Ext.isIE) {
                                                            var file_upl = document.getElementById('bookList.pic-file');
                                                            fileSize = file_upl.fileSize();
                                                            file_upl.select();
                                                            var realpath = document.selection.createRange().text;
                                                            Ext.getCmp("bookList.coverShowBox").getEl().dom.src = realpath;
                                                        } else {
                                                            var node = document.getElementById('bookList.pic-file');
                                                            var file = node.files[0];
                                                            fileSize = file.size;
                                                            Ext.getCmp("bookList.coverShowBox").getEl().dom.src = window.URL.createObjectURL(file);
                                                        }
                                                        if (fileSize > 1024*1024) {
                                                            Ext.Msg.alert('提示', '请上传小于1M的图片文件');
                                                        }
                                                    }
                                                }
                                            },
                                            {
                                                xtype: 'box',
                                                cls: 'xx-box',
                                                id: 'bookList.coverShowBox',
                                                height: "175",
                                                fieldLabel : "预览",
                                                autoEl: {
                                                    tag: 'img'
                                                }
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                layout:'form',
                                xtype: 'fieldset',
                                labelWidth: 60,
                                baseCls: 'xx-fieldset',
                                defaultType: 'textfield',
                                defaults: {
                                    layout: 'form',
                                    border: false,
                                    allowBlank: false,
                                    enableKeyEvents: true,
                                    anchor: '-25' // leave room for error icon
                                },
                                items: [
                                    {
                                        xtype: 'hidden',
                                        id: 'bookList.tagContentIds',
                                        name: 'contentIds',
                                        value: ''
                                    },
                                    {
                                        name: 'tagContentNames',
                                        id: 'bookList.tagContentNames',
                                        fieldLabel: '内容属性',
                                        allowBlank: false,
                                        readOnly: true,
                                        listeners: {
                                            scope: this,
                                            keypress: App.bookList.editTag,
                                            focus: App.bookList.editTag
                                        }
                                    },
                                    {
                                        xtype: 'hidden',
                                        id: 'bookList.tagSupplyIds',
                                        name: 'supplyIds',
                                        value: ''
                                    },
                                    {
                                        name: 'tagSupplyNames',
                                        id: 'bookList.tagSupplyNames',
                                        fieldLabel: '补充属性',
                                        allowBlank: false,
                                        readOnly: true,
                                        listeners: {
                                            scope: this,
                                            keypress: App.bookList.editTag,
                                            focus: App.bookList.editTag
                                        }
                                    },
                                    {
                                        name: 'shortRecommend',
                                        fieldLabel: '短推荐语',
                                        allowBlank: true,
                                        maxLength: 200,
                                        listeners: {
                                            scope: this,
                                            keypress: function (field, e) {
                                                if (e.getKey() == 13) {
                                                    var obj = this.frm.form.findField("longRecommend");
                                                    if (obj) obj.focus();
                                                }
                                            } //keypress
                                        }
                                    },
                                    {
                                        name: 'longRecommend',
                                        fieldLabel: '长推荐语',
                                        allowBlank: true,
                                        maxLength: 200,
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
                                        xtype: 'htmleditor',
                                        name: 'originMemo',
                                        id: 'boolList.form.originMemo',
                                        fieldLabel: '简介',
                                        height: 80,
                                        maxLength: 200,
                                        listeners:{
                                            initialize: function() {
                                                this.getToolbar().hide();
                                            }
                                        }
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        layout:'column',
                        xtype: 'fieldset',
                        title: '版权信息',
                        labelWidth: 80,
                        defaultType: 'textfield',
                        defaults: {
                            layout: 'form',
                            border: false,
                            allowBlank: false,
                            enableKeyEvents: true
                        },
                        items: [
                            {
                                columnWidth: 0.5,
                                layout:'form',
                                xtype: 'fieldset',
                                labelWidth: 60,
                                baseCls: 'xx-fieldset',
                                defaultType: 'textfield',
                                defaults: {
                                    layout: 'form',
                                    border: false,
                                    allowBlank: false,
                                    enableKeyEvents: true,
                                    anchor: '-25' // leave room for error icon
                                },
                                items: [
                                    new Ext.form.ComboBox({
                                        name: 'providerId',
                                        hiddenName: 'providerId',
                                        fieldLabel: '版权',
                                        store: App.bookList.providerStore,
                                        displayField: 'name',
                                        valueField: 'id',
                                        triggerAction: 'all',
                                        allowBlank: false,
                                        editable: false,
                                        listeners: {
                                            'select': App.bookList.changeProvider
                                        }
                                    }),
                                    {
                                        name: 'isbn',
                                        fieldLabel: 'ISBN',
                                        allowBlank: true,
                                        maxLength: 50,
                                        maxLengthText: '最大长度为50字符',
                                        listeners: {
                                            scope: this,
                                            keypress: function (field, e) {
                                                if (e.getKey() == 13) {
                                                    var obj = this.frm.form.findField("batchId");
                                                    if (obj) obj.focus();
                                                }
                                            } //keypress
                                        }
                                    },
                                    new Ext.form.ComboBox({
                                        name: 'batchId',
                                        hiddenName: 'batchId',
                                        id: "bookList.form.batchId",
                                        fieldLabel: '批次号',
                                        store: App.bookList.batchOptionStore,
                                        displayField: 'contractId',
                                        valueField: 'id',
                                        triggerAction: 'all',
                                        allowBlank: false,
                                        editable: false,
                                        listeners: {
                                            "select": App.bookList.changeBatch
                                        }
                                    })
                                ]
                            },
                            {
                                columnWidth: 0.5,
                                layout:'form',
                                xtype: 'fieldset',
                                labelWidth: 80,
                                baseCls: 'xx-fieldset',
                                defaultType: 'textfield',
                                defaults: {
                                    layout: 'form',
                                    border: false,
                                    allowBlank: false,
                                    enableKeyEvents: true,
                                    anchor: '-25' // leave room for error icon
                                },
                                items: [
                                    {
                                        name: 'authorizeStartDate',
                                        fieldLabel: '授权开始时间',
                                        disabled: true
                                    },
                                    {
                                        name: 'authorizeEndDate',
                                        fieldLabel: '授权结束时间',
                                        disabled: true
                                    }
                                ]
                            }
                        ]
                    },
                    {
                        layout:'form',
                        xtype: 'fieldset',
                        title: '价格信息',
                        labelWidth: 60,
                        defaultType: 'textfield',
                        defaults: {
                            layout: 'form',
                            border: false,
                            allowBlank: false,
                            enableKeyEvents: true
                        },
                        items: [
                            {
                                layout:'column',
                                xtype: 'fieldset',
                                baseCls: 'xx-fieldset',
                                defaults: {
                                    layout: 'form',
                                    border: false,
                                    allowBlank: false,
                                    enableKeyEvents: true
                                },
                                items: [
                                    {
                                        columnWidth: 0.5,
                                        layout:'form',
                                        xtype: 'fieldset',
                                        labelWidth: 60,
                                        defaultType: 'textfield',
                                        defaults: {
                                            layout: 'form',
                                            border: false,
                                            allowBlank: false,
                                            enableKeyEvents: true,
                                            anchor: '-25' // leave room for error icon
                                        },
                                        items: [
                                            new Ext.form.ComboBox({
                                                hiddenName: 'isFee',
                                                fieldLabel: '是否收费',
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
                                                editable: false,
                                                listeners: {
                                                    "select": App.bookList.changeIsFee
                                                }
                                            }),
                                            {
                                                name: 'wholePrice',
                                                fieldLabel: '全本价格',
                                                allowBlank: true,
                                                maxLength: 50,
                                                maxLengthText: '最大长度为25字符'
                                            }
                                        ]
                                    },
                                    {
                                        columnWidth: 0.5,
                                        layout:'form',
                                        xtype: 'fieldset',
                                        labelWidth: 80,
                                        defaultType: 'textfield',
                                        defaults: {
                                            layout: 'form',
                                            border: false,
                                            allowBlank: false,
                                            enableKeyEvents: true,
                                            anchor: '-25' // leave room for error icon
                                        },
                                        items: [
                                            new Ext.form.ComboBox({
                                                hiddenName: 'isWholeFee',
                                                fieldLabel: '全本收费',
                                                store: new Ext.data.SimpleStore({
                                                    fields: ['text', 'value'],
                                                    data: [
                                                        ['是', 1],
                                                        ['否', 0]
                                                    ]
                                                }),
                                                mode: 'local',
                                                displayField: 'text',
                                                valueField: 'value',
                                                triggerAction: 'all',
                                                editable: false
                                            }),
                                            {
                                                name: 'feeChapter',
                                                fieldLabel: '收费起始章节',
                                                allowBlank: true,
                                                maxLength: 10,
                                                regex:/^[1-9]\d*$/,
                                                regexText:'请输入大于0整数',
                                                maxLengthText: '最大长度为25字符'
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                xtype: 'fieldset',
                                id: 'bookList.feePlatformFieldSet',
                                fieldLabel: '收费平台',
                                labelWidth: 60,
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
                        layout:'form',
                        xtype: 'fieldset',
                        title: '其它信息',
                        labelWidth: 60,
                        defaultType: 'textfield',
                        defaults: {
                            layout: 'form',
                            border: false,
                            allowBlank: false,
                            enableKeyEvents: true
                        },
                        items: [
                            {
                                layout:'column',
                                xtype: 'fieldset',
                                baseCls: 'xx-fieldset',
                                defaults: {
                                    layout: 'form',
                                    border: false,
                                    allowBlank: false,
                                    enableKeyEvents: true
                                },
                                items: [
                                    {
                                        columnWidth: 0.5,
                                        layout:'form',
                                        xtype: 'fieldset',
                                        labelWidth: 60,
                                        defaultType: 'textfield',
                                        defaults: {
                                            layout: 'form',
                                            border: false,
                                            allowBlank: false,
                                            enableKeyEvents: true,
                                            anchor: '-25' // leave room for error icon
                                        },
                                        items: [
                                            {
                                                xtype: 'hidden',
                                                name: 'oldStatus',
                                                value: ''
                                            },
                                            new Ext.form.ComboBox({
                                                hiddenName: 'status',
                                                fieldLabel: '状态',
                                                store: new Ext.data.SimpleStore({
                                                    fields: ['text', 'value'],
                                                    data: [
                                                        ['入库', '0'],
                                                        ['上线', '2'],
                                                        ['下线', '3']
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
                                                        var oldStatus = this.frm.form.findField("oldStatus").getValue();

                                                        if (oldStatus != 0 && r.data.value == 0) {
                                                            c.setValue(oldStatus);
                                                            Ext.Msg.alert('提示', '不能选择入库状态');
                                                        }
                                                    }
                                                }
                                            })
                                        ]
                                    },
                                    {
                                        columnWidth: 0.5,
                                        layout:'form',
                                        xtype: 'fieldset',
                                        labelWidth: 80,
                                        defaultType: 'textfield',
                                        defaults: {
                                            layout: 'form',
                                            border: false,
                                            allowBlank: false,
                                            enableKeyEvents: true,
                                            anchor: '-25' // leave room for error icon
                                        },
                                        items: [
                                            {
                                                name: 'dayPublishChapters',
                                                fieldLabel: '日发布章节数',
                                                allowBlank: true,
                                                maxLength: 50,
                                                maxLengthText: '最大长度为25字符'
                                            }
                                        ]
                                    }
                                ]
                            },
                            {
                                xtype: 'fieldset',
                                id: 'bookList.operatePlatformFieldSet',
                                fieldLabel: '运营平台',
                                labelWidth: 60,
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
                    }
                ],//items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            if (this.frm.form.isValid()) {
                                if (!App.bookList.platformSubmitCheck()) {
                                    return;
                                }
                                var isFee = this.frm.form.findField("isFee").getValue();
                                var feeChapter = this.frm.form.findField("feeChapter").getValue();
                                if (isFee == 1 && (feeChapter == '' || feeChapter == 0)) {
                                    Ext.Msg.alert('提示:', '书籍收费时，请设置收费起始章节');
                                    return;
                                }

                                var isWholeFee = this.frm.form.findField("isWholeFee").getValue();
                                var wholePrice = this.frm.form.findField("wholePrice").getValue();
                                if (isWholeFee == 0 && wholePrice > 0) {
                                    Ext.Msg.alert('提示:', '全本免费时，请设置全本价格为0');
                                    return;
                                }
                                if (isFee == 1 && isWholeFee == 0) {
                                    var feeChapter = this.frm.form.findField("feeChapter").getValue();
                                    if (feeChapter == '') {
                                        Ext.Msg.alert('提示:', '请设置收费起始章节');
                                        return;
                                    }
                                }

                                var fileSize = 0;
                                if (Ext.isIE) {
                                    var file = Ext.getCmp('bookList.pic');
                                    if (file.getValue() != '') {
                                        var file_upl = document.getElementById('bookList.pic-file');
                                        fileSize = file_upl.fileSize();
                                    }
                                } else {
                                    var node = document.getElementById('bookList.pic-file');
                                    if (node.files.length > 0) {
                                        var file = node.files[0];
                                        fileSize = file.size;
                                    }
                                }
                                if (fileSize > 1024*1024) {
                                    Ext.Msg.alert('提示', '请上传小于1M的图片文件');
                                    return;
                                }

                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.bookList.store.reload();
                                        App.bookList.dlg.hide();
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
            });

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
                                        if (checked) {
                                            App.bookList.temporaryValues.formTagClassifyId = checked.inputValue;
                                            App.bookList.temporaryValues.formTagClassifyName = checked.alias;
                                        }
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
                                        if (checked) {
                                            App.bookList.temporaryValues.formTagSexId = checked.inputValue;
                                            App.bookList.temporaryValues.formTagSexName = checked.boxLabel;
                                        }
                                    }
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'fieldset',
                        id: 'bookList.tagContentFieldSet',
                        title: '内容属性',
                        labelWidth: 80,
                        layout: 'form',
                        collapsed: false,
                        collapsible: false,
                        items: []
                    },
                    {
                        xtype: 'fieldset',
                        id: 'bookList.tagSupplyFieldSet',
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
                                if (App.bookList.tagSubmitCheck('bookList.tagNames', App.bookList.tagContentStore, 3) &&
                                    App.bookList.tagSubmitCheck('bookList.tagNames', App.bookList.tagSupplyStore, 4)) {

                                    var field = Ext.getCmp('bookList.tagSexName');
                                    if (App.bookList.temporaryValues.formTagSexId != '') {
                                        field.setValue(App.bookList.temporaryValues.formTagSexName);
                                        field = Ext.getCmp('bookList.tagSexId');
                                        field.setValue(App.bookList.temporaryValues.formTagSexId);
                                    }

                                    if (App.bookList.temporaryValues.formTagClassifyId != '') {
                                        field = Ext.getCmp('bookList.tagClassifyName');
                                        field.setValue(App.bookList.temporaryValues.formTagClassifyName);
                                        field = Ext.getCmp('bookList.tagClassifyId');
                                        field.setValue(App.bookList.temporaryValues.formTagClassifyId);
                                    }

                                    if (App.bookList.temporaryValues.formTagContentIds) {
                                        field = Ext.getCmp('bookList.tagContentNames');
                                        field.setValue(App.bookList.temporaryValues.formTagContentNames);
                                        field = Ext.getCmp('bookList.tagContentIds');
                                        field.setValue(App.bookList.temporaryValues.formTagContentIds);
                                    }

                                    if (App.bookList.temporaryValues.formTagStoryId != '') {
                                        field = Ext.getCmp('bookList.tagStoryName');
                                        field.setValue(App.bookList.temporaryValues.formTagStoryName);
                                        field = Ext.getCmp('bookList.tagStoryId');
                                        field.setValue(App.bookList.temporaryValues.formTagStoryId);
                                    }

                                    if (App.bookList.temporaryValues.formTagSupplyIds != '') {
                                        field = Ext.getCmp('bookList.tagSupplyNames');
                                        field.setValue(App.bookList.temporaryValues.formTagSupplyNames);
                                        field = Ext.getCmp('bookList.tagSupplyIds');
                                        field.setValue(App.bookList.temporaryValues.formTagSupplyIds);
                                    }

                                    App.bookList.tdlg.hide();
                                }
                            }
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            App.bookList.tdlg.hide();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return tfrm;
        },

        getMultiAddTagForm: function () {
            var mtfrm = new Ext.form.FormPanel({
                fileUpload: true,
                url: appPath + '/book/multiAddTag.do',
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
                    new Ext.form.ComboBox({
                        name: 'providerId',
                        hiddenName: 'providerId',
                        fieldLabel: '版权',
                        store: App.bookList.providerStore,
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        allowBlank: false,
                        editable: false
                    }),
                    {
                        xtype: 'fileuploadfield',
                        fieldLabel: 'excel文件',
                        name: 'file',
                        id: 'bookList.upload.fileUpload2',
                        width: 400,
                        emptyText: '选择excel文件...',
                        buttonText: '',
                        buttonCfg: {
                            iconCls: 'icon-search'
                        },
                        allowBlank: false
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            if (this.mtfrm.form.isValid()) {
                                this.mtfrm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.bookList.store.load();
                                        App.bookList.mtdlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                    },
                                    timeout: 90,
                                    waitMsg: '正在保存数据，请稍后...'
                                });
                            }
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            this.mtfrm.form.reset();
                            this.mtfrm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return mtfrm;
        },

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 600,
                height: 600,
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
                    },
                    beforehide: function () {
                        Ext.getCmp("bookList.coverShowBox").getEl().dom.src = Ext.BLANK_IMAGE_URL;
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
                    },
                    beforehide: function () {
                        App.bookList.emptyTemporaryValues();
                    },
                    beforeclose: function () {
                        App.bookList.emptyTemporaryValues();
                    }
                }
            }); //dlg
            return tdlg;
        },

        getMultiAddTagDialog: function () {
            var mtdlg = new Ext.Window({
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
                items: [this.mtfrm],
                listeners: {
                    scope: this,
                    show: function () {
                        this.mtfrm.form.reset();
                        this.mtfrm.form.clearInvalid();
                    }
                }
            }); //dlg
            return mtdlg;
        },

        createGrid: function (id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    {
                        xtype : 'buttongroup',
                        columns : 2,
                        height : 50,
                        items : [
                            {xtype : 'tbtext', text:'书ID：'},
                            {
                                xtype: 'textfield',
                                id: 'bookList.search.id',
                                width: 80,
                                enableKeyEvents:true,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            App.bookList.store.load();
                                        }
                                    } //keypress
                                }
                            },
                            {xtype : 'tbtext', text:'状态：'},
                            new Ext.form.ComboBox({
                                id: 'bookList.search.status',
                                store: new Ext.data.SimpleStore({
                                    fields: ['text', 'value'],
                                    data: [
                                        ['入库', '0'],
                                        ['上线', '2'],
                                        ['下线', '3'],
                                        ['全部', '']
                                    ]
                                }),
                                mode: 'local',
                                displayField: 'text',
                                valueField: 'value',
                                triggerAction: 'all',
                                width: 80
                            })
                        ]
                    },
                    {
                        xtype : 'buttongroup',
                        columns : 2,
                        height : 50,
                        items : [
                            {xtype : 'tbtext', text:'书名：'},
                            {
                                xtype: 'textfield',
                                id: 'bookList.search.name',
                                width: 80,
                                enableKeyEvents:true,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            App.bookList.store.load();
                                        }
                                    } //keypress
                                }
                            },
                            {xtype : 'tbtext', text:'作者：'},
                            {
                                xtype: 'textfield',
                                id: 'bookList.search.author',
                                width: 80,
                                enableKeyEvents:true,
                                listeners: {
                                    scope: this,
                                    keypress: function (field, e) {
                                        if (e.getKey() == 13) {
                                            App.bookList.store.load();
                                        }
                                    } //keypress
                                }
                            }
                        ]
                    },
                    {
                        xtype : 'buttongroup',
                        columns : 2,
                        height : 50,
                        items : [
                            {xtype : 'tbtext', text:'类别：'},
                            new Ext.form.ComboBox({
                                id: 'bookList.search.tagClassifyId',
                                store: new Ext.data.SimpleStore({
                                    fields: ['text', 'value'],
                                    data: [
                                        ['出版-非小说', '102'],
                                        ['出版-小说', '101'],
                                        ['原创-非小说', '104'],
                                        ['原创-小说', '103'],
                                        ['全部', '']
                                    ]
                                }),
                                mode: 'local',
                                displayField: 'text',
                                valueField: 'value',
                                triggerAction: 'all',
                                width: 80
                            }),
                            {xtype : 'tbtext', text:'性别：'},
                            new Ext.form.ComboBox({
                                id: 'bookList.search.tagSexId',
                                store: new Ext.data.SimpleStore({
                                    fields: ['text', 'value'],
                                    data: [
                                        ['男频', '105'],
                                        ['女频', '106'],
                                        ['综合', '107'],
                                        ['全部', '']
                                    ]
                                }),
                                mode: 'local',
                                displayField: 'text',
                                valueField: 'value',
                                triggerAction: 'all',
                                width: 80
                            })
                        ]
                    },
                    {
                        xtype : 'buttongroup',
                        columns : 2,
                        height : 50,
                        items : [
                            {xtype : 'tbtext', text:'故事：'},
                            new Ext.form.ComboBox({
                                id: "bookList.search.tagStoryId",
                                store: new Ext.data.Store({
                                    autoLoad: false,
                                    baseParams: {
                                        isUse: 1,
                                        parentId: 4
                                    },
                                    proxy: new Ext.data.HttpProxy({
                                        url: appPath + '/tag/list.do'
                                    }),
                                    remoteSort: false,
                                    reader: new Ext.data.JsonReader({
                                        totalProperty: 'totalItems',
                                        root: 'result',
                                        idProperty: 'id',
                                        fields: ['id', 'name']
                                    }),
                                    listeners: {'load': function () {
                                        this.add(newRecord);
                                    }
                                    }
                                }),
                                displayField: 'name',
                                valueField: 'id',
                                triggerAction: 'all',
                                editable: false,
                                width: 80
                            }),
                            {xtype : 'tbtext', text:'分类：'},
                            new Ext.form.ComboBox({
                                id: "bookList.search.categoryId",
                                store: new Ext.data.Store({
                                    autoLoad: false,
                                    baseParams: {
                                        isUse: 1,
                                        isLeaf: 1
                                    },
                                    proxy: new Ext.data.HttpProxy({
                                        url: appPath + '/category/list.do'
                                    }),
                                    remoteSort: false,
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
                                }),
                                displayField: 'name',
                                valueField: 'id',
                                triggerAction: 'all',
                                editable: false,
                                width: 80
                            })
                        ]
                    },
                    {
                        xtype : 'buttongroup',
                        columns : 2,
                        height : 50,
                        items : [
                            {xtype : 'tbtext', text:'版权方：'},
                            new Ext.form.ComboBox({
                                id: "bookList.search.providerId",
                                store: new Ext.data.Store({
                                    autoLoad: false,
                                    baseParams: {
                                        isUse: 1
                                    },
                                    proxy: new Ext.data.HttpProxy({
                                        url: appPath + '/provider/list.do'
                                    }),
                                    remoteSort: false,
                                    reader: new Ext.data.JsonReader({
                                        totalProperty: 'totalItems',
                                        root: 'result',
                                        idProperty: 'id',
                                        fields: ['id', 'name']
                                    }),
                                    listeners: {'load': function () {
                                        this.add(newRecord);
                                    }
                                    }
                                }),
                                displayField: 'name',
                                valueField: 'id',
                                triggerAction: 'all',
                                editable: false,
                                width: 90
                            }),
                            {xtype : 'tbtext', text:'连载：'},
                            new Ext.form.ComboBox({
                                id: 'bookList.search.isSerial',
                                store: new Ext.data.SimpleStore({
                                    fields: ['text', 'value'],
                                    data: [
                                        ['否', '0'],
                                        ['是', '1'],
                                        ['全部', '']
                                    ]
                                }),
                                mode: 'local',
                                displayField: 'text',
                                valueField: 'value',
                                triggerAction: 'all',
                                width: 90
                            })
                        ]
                    },
                    {
                        xtype : 'buttongroup',
                        columns : 2,
                        height : 50,
                        items : [
                            {xtype : 'tbtext', text:'更新时间起：'},
                            {
                                xtype:'datetimefield',
                                id:'bookList.search.startEditDate',
                                format:'Y-m-d',
                                width:130
                            },
                            {xtype : 'tbtext', text:'更新时间止：'},
                            {
                                xtype:'datetimefield',
                                id:'bookList.search.endEditDate',
                                format:'Y-m-d H:i:s',
                                width:130
                            }
                        ]
                    },
                    {
                        xtype : 'buttongroup',
                        columns : 2,
                        height : 50,
                        items : [
                            {xtype : 'tbtext', text:'运营平台：'},
                            new Ext.form.ComboBox({
                                id: "bookList.search.operatePlatformId",
                                store: new Ext.data.Store({
                                    autoLoad: false,
                                    baseParams: {
                                        isUse: 1
                                    },
                                    proxy: new Ext.data.HttpProxy({
                                        url: appPath + '/platform/list.do'
                                    }),
                                    remoteSort: false,
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
                                }),
                                displayField: 'name',
                                valueField: 'id',
                                triggerAction: 'all',
                                editable: false,
                                width: 90
                            }),
                            {xtype : 'tbtext', text:'是否收费：'},
                            new Ext.form.ComboBox({
                                id: 'bookList.search.isFee',
                                store: new Ext.data.SimpleStore({
                                    fields: ['text', 'value'],
                                    data: [
                                        ['否', '0'],
                                        ['是', '1'],
                                        ['全部', '']
                                    ]
                                }),
                                mode: 'local',
                                displayField: 'text',
                                valueField: 'value',
                                triggerAction: 'all',
                                width: 90
                            })
                        ]
                    },
                    {
                        xtype: 'buttongroup',
                        columns: 1,
                        height: 50,
                        items: [
                            {
                                text: '查找', iconCls: 'icon-search',
                                handler: function () {
                                    App.bookList.store.load();
                                }
                            }
                        ]
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
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量上线',
                        iconCls: 'icon-page_go',
                        id: 'bookList.button.batchPublish',
                        handler: this.online
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量下线',
                        iconCls: 'icon-page_red',
                        id: 'bookList.button.batchAudit',
                        handler: this.offline
                    },
                    /*
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量删除',
                        iconCls: 'icon-page_delete',
                        handler: this.del
                    },
                    */
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '批量打标',
                        iconCls: 'icon-page_excel',
                        handler: this.multiAddTag
                    }
                ],

                store: this.store,
                sm: sm,
                columns: [sm,
                    {
                        header: "操作",
                        width: 80,
                        sortable: false,
                        dataIndex: 'id',
                        align: 'center',
                        renderer: optRender
                    },
                    {
                        header: "ID",
                        width: 50,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "书名",
                        width: 180,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center',
                        listeners: {
                            click : App.bookList.edit
                        }
                    },
                    {
                        header: "作者",
                        width: 100,
                        sortable: true,
                        dataIndex: 'author',
                        align: 'center'
                    },
                    {
                        header: "类别属性",
                        width: 70,
                        sortable: true,
                        dataIndex: 'tagClassifyName',
                        align: 'center'
                    },
                    {
                        header: "性别属性",
                        width: 60,
                        sortable: true,
                        dataIndex: 'tagSexName',
                        align: 'center'
                    },
                    {
                        header: "故事类型",
                        width: 60,
                        sortable: true,
                        dataIndex: 'tagStoryName',
                        align: 'center'
                    },
                    {
                        header: "连载",
                        width: 40,
                        sortable: true,
                        dataIndex: 'isSerial',
                        align: 'center',
                        renderer: App.yesRender
                    },
                    {
                        header: "收费",
                        width: 40,
                        sortable: true,
                        dataIndex: 'isFee',
                        align: 'center',
                        renderer: App.yesRender
                    },
                    {
                        header: "章节数",
                        width: 50,
                        sortable: true,
                        dataIndex: 'chapters',
                        align: 'center'
                    },
                    {
                        header: "发布章节数",
                        width: 70,
                        sortable: true,
                        dataIndex: 'publishChapters',
                        align: 'center'
                    },
                    {
                        header: "章节更新时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'updateChapterDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },
                    {
                        header: "书籍更新时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'editDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },
                    {
                        header: "状态",
                        width: 40,
                        sortable: true,
                        dataIndex: 'status',
                        align: 'center',
                        renderer: this.statusRender
                    },
                    {
                        header: "版权方",
                        width: 70,
                        sortable: true,
                        dataIndex: 'providerName',
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

            function nameRender(value, p, record) {
                var html = '<a href=""';
            }

            function optRender(value, p, record) {
                var html = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.bookList.edit(' + value + ')"/>';
                html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/comments.png" title="推荐记录" onclick="App.bookList.showRecommendLog(' + value + ')"/>';
                html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/book_open.png" title="章节" onclick="App.bookList.showChapters(' + value + ','+ record.data.chapters +','+record.data.publishChapters+')"/>';
                return String.format(html);
            }

            panel.add(this.grid);

        },

        add: function () {
            App.bookList.initTagCounter('', '');

            App.bookList.temporaryValues.tagCount = 0;
            App.bookList.temporaryValues.tagContentCount = 0;
            App.bookList.buildCheckGroup('', 'bookList.tagContentFieldSet', 'bookList.tagNames',
                'formTagContentIds', 'formTagContentNames',
                App.bookList.tagContentStore, 3);
            App.bookList.temporaryValues.tagContentCount = App.bookList.temporaryValues.tagCount;

            App.bookList.temporaryValues.tagCount = 0;
            App.bookList.temporaryValues.tagSupplyCount = 0;
            App.bookList.buildCheckGroup('', 'bookList.tagSupplyFieldSet', 'bookList.tagNames',
                'formTagSupplyIds', 'formTagSupplyNames',
                App.bookList.tagSupplyStore, 4);
            App.bookList.temporaryValues.tagSupplyCount = App.bookList.temporaryValues.tagCount;

            Ext.apply(App.bookList.currentFormValues, {
                id: "",
                cpBookId: "",
                name: "",
                author: "",
                isSerial: "",
                words: "0",
                checkLevel: "",
                onlineDate: "",
                tagStoryName: "",
                tagClassifyId: "",
                tagClassifyName: "",
                tagSexId: "",
                tagSexName: "",
                contentIds: "",
                tagContentNames: "",
                supplyIds: "",
                tagSupplyNames: "",
                shortRecommend: "",
                longRecommend: "",
                memo: "",
                providerId: "",
                batchId: "",
                updateChapter: "",
                updateChapterId: "",
                updateChapterDate: "",
                isbn: "",
                authorizeStartDate: "",
                authorizeEndDate: "",
                isFee: "0",
                isWholeFee: "0",
                feeChapter: "",
                dayPublishChapters: "2",
                wholePrice: "0",
                oldStatus: "0",
                status: "0"
            });

            App.bookList.frm.form.reset();

            App.bookList.frm.getForm().findField('id').disable();
            App.bookList.frm.getForm().findField('batchId').disable();

            App.bookList.buildSimpleCheckGroup("", "bookList.feePlatformFieldSet", "feePlatformIds");
            App.bookList.buildSimpleCheckGroup("", "bookList.operatePlatformFieldSet", "operatePlatformIds");

            App.bookList.dlg.setTitle("增加书籍");
            App.bookList.dlg.show();
            Ext.getCmp("bookList.coverShowBox").getEl().dom.src = Ext.BLANK_IMAGE_URL;
        },

        edit: function () {
            if (App.bookList.grid.getSelectionModel().hasSelection()) {
                App.bookList.dlg.setTitle("编辑书籍");
                var rec = App.bookList.grid.getSelectionModel().getSelected();

                Ext.Ajax.request({
                    method: 'post',
                    url: appPath + '/book/load.do',
                    params: {
                        id: rec.data.id
                    },
                    success: function (resp, opts) {
                        var result = Ext.util.JSON.decode(resp.responseText);
                        if (result.success == 'true') {
                            var o = result.object;
                            var onlineDate = '';
                            if (o.onlineDate) {
                                onlineDate = new Date(o.onlineDate);
                            }
                            App.bookList.setProvider(o.providerId);
                            App.bookList.frm.getForm().findField('id').enable();
                            App.bookList.buildSimpleCheckGroup(o.feePlatformIds, "bookList.feePlatformFieldSet", "feePlatformIds");
                            App.bookList.buildSimpleCheckGroup(o.operatePlatformIds, "bookList.operatePlatformFieldSet", "operatePlatformIds");
                            Ext.apply(App.bookList.currentFormValues, {
                                id: o.id,
                                cpBookId: o.cpBookId,
                                name: o.name,
                                author: o.author,
                                isSerial: o.isSerial,
                                words: o.words,
                                checkLevel: o.checkLevel,
                                onlineDate: onlineDate,
                                tagStoryName: o.tagStoryName,
                                tagClassifyId: o.tagClassifyId,
                                tagClassifyName: o.tagClassifyName,
                                tagSexId: o.tagSexId,
                                tagSexName: o.tagSexName,
                                tagContentNames: o.tagContentNames,
                                contentIds: o.tagContentIds,
                                tagSupplyNames: o.tagSupplyNames,
                                supplyIds: o.tagSupplyIds,
                                shortRecommend: o.shortRecommend,
                                longRecommend: o.longRecommend,
                                originMemo: Ext.util.Format.htmlDecode(o.originMemo),
                                providerId: o.providerId,
                                batchId: o.batchId,
                                updateChapter: o.updateChapter,
                                updateChapterId: o.updateChapterId,
                                updateChapterDate:o.updateChapterDate,
                                isbn: o.isbn,
                                authorizeStartDate: o.authorizeStartDate,
                                authorizeEndDate: o.authorizeEndDate,
                                isFee: o.isFee,
                                isWholeFee: o.isWholeFee,
                                feeChapter: o.feeChapter,
                                dayPublishChapters: o.dayPublishChapters,
                                wholePrice: o.wholePrice,
                                oldStatus: o.status,
                                status: o.status
                            });

                            App.bookList.frm.getForm().findField('batchId').setValue(o.batchId);

                            App.bookList.initTagCounter(o.tagContentIds, o.tagSupplyIds);

                            App.bookList.temporaryValues.tagCount = 0;
                            App.bookList.temporaryValues.tagContentCount = 0;
                            App.bookList.buildCheckGroup(o.tagContentIds, 'bookList.tagContentFieldSet', 'bookList.tagNames',
                                'formTagContentIds', 'formTagContentNames',
                                App.bookList.tagContentStore, 3);
                            App.bookList.temporaryValues.tagContentCount = App.bookList.temporaryValues.tagCount;

                            App.bookList.temporaryValues.tagCount = 0;
                            App.bookList.temporaryValues.tagSupplyCount = 0;
                            App.bookList.buildCheckGroup(o.tagSupplyIds, 'bookList.tagSupplyFieldSet', 'bookList.tagNames',
                                'formTagSupplyIds', 'formTagSupplyNames',
                                App.bookList.tagSupplyStore, 4);
                            App.bookList.temporaryValues.tagSupplyCount = App.bookList.temporaryValues.tagCount;

                            App.bookList.dlg.show();
                            Ext.getCmp("bookList.coverShowBox").getEl().dom.src = o.smallPic;
                        } else {
                            Ext.Msg.alert('信息', result.info);
                        }
                    },
                    failure: function (resp, opts) {
                        Ext.Msg.alert('信息', '取书籍数据失败');
                    }
                });
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的书籍');
            }
        },

        editTag: function () {
            var tagClassifyId = App.bookList.frm.form.findField("tagClassifyId").getValue();
            var tagSexId = App.bookList.frm.form.findField("tagSexId").getValue();
            var tagContentIds = App.bookList.frm.form.findField("contentIds").getValue();
            var tagSupplyIds = App.bookList.frm.form.findField("supplyIds").getValue();

            App.bookList.tfrm.form.findField("tagClassifyId").setValue(tagClassifyId);
            App.bookList.tfrm.form.findField("tagSexId").setValue(tagSexId);

            App.bookList.tdlg.setTitle("编辑标签");
            App.bookList.tdlg.show();
        },

        del: function() {
            if (App.bookList.grid.getSelectionModel().hasSelection()) {
                var recs = App.bookList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    words += data.name + '<br>';
                }
                Ext.Msg.confirm('批量上线书籍', '确定删除所选的'+recs.length+'本书籍？',
                    function (btn) {
                        if (btn == 'yes') {
                            App.mask.show();
                            Ext.Ajax.request({
                                method: 'post',
                                timeout: 90000,
                                url: appPath + '/book/multiOpt.do',
                                params: {
                                    opt: 'del',
                                    ids: ids.toString(),
                                    status:3
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.bookList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要删除的书籍！');
            }
        },

        online: function() {
            if (App.bookList.grid.getSelectionModel().hasSelection()) {
                var recs = App.bookList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    if (data.tagClassifyId == '') {
                        Ext.Msg.alert('信息', '《'+data.name + '》，信息未完整，请审核');
                        return;
                    }

                    ids.push(data.id);
                    words += data.name + '<br>';
                }
                Ext.Msg.confirm('批量上线书籍', '确定上线所选的'+recs.length+'本书籍？',
                    function (btn) {
                        if (btn == 'yes') {
                            App.mask.show();
                            Ext.Ajax.request({
                                method: 'post',
                                timeout: 90000,
                                url: appPath + '/book/multiOpt.do',
                                params: {
                                    opt: 'online',
                                    ids: ids.toString(),
                                    status:3
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.bookList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要上线的书籍！');
            }
        },

        offline: function() {
            if (App.bookList.grid.getSelectionModel().hasSelection()) {
                var recs = App.bookList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    if (data.status != 2) {
                        Ext.Msg.alert('信息', data.name + '，为非上线状态');
                        return;
                    }
                    ids.push(data.id);
                    words += data.name + '<br>';
                }
                Ext.Msg.confirm('批量下线书籍', '确定下线所选的'+recs.length+'本书籍？',
                    function (btn) {
                        if (btn == 'yes') {
                            App.mask.show();
                            Ext.Ajax.request({
                                method: 'post',
                                timeout: 90000,
                                url: appPath + '/book/multiOpt.do',
                                params: {
                                    opt: 'offline',
                                    ids: ids.toString(),
                                    status:4
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.bookList.store.load();
                                    } else {
                                        Ext.Msg.alert('信息', info);
                                    }
                                },
                                failure: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    App.mask.hide();
                                    Ext.Msg.alert('提示', info);
                                }
                            });
                        }
                    });
            } else {
                Ext.Msg.alert('信息', '请选择要下线的书籍！');
            }
        },

        multiAddTag: function() {
            App.bookList.mtdlg.setTitle("批量打标");
            App.bookList.mtdlg.show();
        },

        exportExcel:  function() {
            var id = Ext.getCmp("bookList.id").getValue();
            var categoryId = Ext.getCmp("bookList.categoryId").getValue();
            var channelId = Ext.getCmp("bookList.channelId").getValue();
            var author = Ext.getCmp("bookList.author").getValue();
            var isSerial = Ext.getCmp("bookList.isSerial").getValue();
            var isFee = Ext.getCmp("bookList.isFee").getValue();
            var isWholeFee = Ext.getCmp("bookList.isWholeFee").getValue();
            var name = Ext.getCmp("bookList.name").getValue();
            var status = Ext.getCmp("bookList.status").getValue();
            var startCreateDate = Ext.getCmp('bookList.startCreateDate').getValue();
            var endCreateDate = Ext.getCmp('bookList.endCreateDate').getValue();
            var orderField = Ext.getCmp("bookList.orderField").getValue();
            var orderType = Ext.getCmp("bookList.orderType").getValue();
            var cpBookId = Ext.getCmp("bookList.cpBookId").getValue();

            var query = '?1=1';
            if (id != '') {
                query = query + '&id='+id;
            }
            if (categoryId != '') {
                query = query + '&categoryId='+categoryId;
            }
            if (channelId != '') {
                query = query + '&channelId='+channelId;
            }
            if (author != '') {
                query = query + '&author='+author;
            }
            if (isSerial != '') {
                query = query + '&isSerial='+isSerial;
            }
            if (isFee != '') {
                query = query + '&isFee='+isFee;
            }
            if (isWholeFee != '') {
                query = query + '&isWholeFee='+isWholeFee;
            }
            if (name != '') {
                query = query + '&name='+name;
            }
            if (status != '') {
                query = query + '&status='+status;
            }
            if (startCreateDate != '') {
                query = query + '&startCreateDate='+App.dateFormat(startCreateDate);
            }
            if (endCreateDate != '') {
                query = query + '&endCreateDate='+App.dateFormat(endCreateDate);
            }
            if (orderField != '') {
                query = query + '&orderField='+orderField;
            }
            if (orderType != '') {
                query = query + '&orderType='+orderType;
            }
            if (cpBookId != '') {
                query = query + '&cpBookId='+cpBookId;
            }

            window.open(appPath + '/book/export.do' + query);
        },

        refresh: function () {
            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function (btn) {
                if (btn == 'yes') {
                    var requestConfig = {
                        url: appPath + '/book/refresh.do',
                        callback: function (o, s, r) {
                            var result = Ext.util.JSON.decode(r.responseText);
                            Ext.Msg.alert('提示', result.info);
                        }
                    }
                    Ext.Ajax.request(requestConfig);
                }
            });
        },

        delAllOffline: function () {
            Ext.MessageBox.confirm('请确认', '您确认执行删除下线操作吗？', function (btn) {
                if (btn == 'yes') {
                    var requestConfig = {
                        url: appPath + '/book/delAllOffline.do',
                        callback: function (o, s, r) {
                            var result = Ext.util.JSON.decode(r.responseText);
                            if (result.success == 'true') {
                                App.bookList.store.reload();
                            }
                            Ext.Msg.alert('提示', result.info);
                        }
                    }
                    Ext.Ajax.request(requestConfig);
                }
            });
        },

        setProvider: function(id) {
            App.bookList.frm.getForm().findField('batchId').enable();
            App.bookList.frm.getForm().findField('batchId').reset();
            App.bookList.batchOptionStore.proxy = new Ext.data.HttpProxy({url: appPath + '/batch/list.do?providerId=' + id});
            App.bookList.batchOptionStore.load();
        },

        changeProvider: function () {
            App.bookList.frm.getForm().findField('batchId').enable();
            App.bookList.frm.getForm().findField('batchId').reset();
            var providerId = App.bookList.frm.form.findField("providerId").getValue();
            if (providerId == '') {
                providerId = App.bookList.currentFormValues.providerId;
            }

            App.bookList.batchOptionStore.proxy = new Ext.data.HttpProxy({url: appPath + '/batch/list.do?providerId=' + providerId});
            App.bookList.batchOptionStore.load();
        },

        changeIsSerial: function() {
            var isSerial = App.bookList.frm.form.findField("isSerial").getValue();
            var isWholeFeeField = App.bookList.frm.form.findField("isWholeFee");
            if (isSerial == 1) {
                isWholeFeeField.setValue(0);
                isWholeFeeField.disable();
            } else {
                isWholeFeeField.enable();
            }
        },

        changeBatch: function() {
            var batchId = App.bookList.frm.getForm().findField('batchId').getValue();
            Ext.Ajax.request({
                method: 'post',
                url: appPath + '/batch/load.do',
                params: {
                    id: batchId
                },
                success: function (resp, opts) {
                    var result = Ext.util.JSON.decode(resp.responseText);
                    if (result.success == 'true') {
                        var o = result.object;
                        App.bookList.frm.getForm().findField('authorizeStartDate').setValue(new Date(o.authorizeStartDate).format('Y-m-d'));
                        App.bookList.frm.getForm().findField('authorizeEndDate').setValue(new Date(o.authorizeEndDate).format('Y-m-d'));
                    } else {
                        Ext.Msg.alert('信息', result.info);
                    }
                },
                failure: function (resp, opts) {
                    Ext.Msg.alert('信息', '取书籍数据失败');
                }
            });
        },

        changeIsFee: function() {
            var isFee = App.bookList.frm.form.findField("isFee").getValue();
            var isWholeFeeField = App.bookList.frm.form.findField("isWholeFee");
            var wholePriceField = App.bookList.frm.form.findField("wholePrice");
            if (isFee == 0) {
                isWholeFeeField.setValue(0);
                isWholeFeeField.setReadOnly(true);
                wholePriceField.setValue(0);
                wholePriceField.setReadOnly(true);

                var size = App.bookList.platformStore.getTotalCount();
                for (var i = 0; i < size; i++) {
                    var id = "bookList.boxModule_feePlatformIds_" + i;
                    var box = Ext.getCmp(id);
                    box.setValue(false);
                }
            } else {
                isWholeFeeField.setReadOnly(false);
                wholePriceField.setValue(0);
                wholePriceField.setReadOnly(false);
            }
        },

        changeStatus: function() {
            var status = App.bookList.frm.form.findField("status").getValue();
            var oldStatus = App.bookList.frm.form.findField("oldStatus").getValue();
            if (oldStatus != 0) {
                App.bookList.frm.form.findField("status").setValues(oldStatus);
                Ext.Msg.alert('提示', '不能选择入库状态');
            }
        },

        buildSimpleCheckGroup: function (values, fieldSetId, name) {
            var length = 0;
            if (values) {
                length = values.length;
            }

            var size = this.platformStore.getTotalCount();
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
                    moduleId = this.platformStore.getAt(i).data.id;
                    moduleName = this.platformStore.getAt(i).data.name;
                    if (values) {
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
                    id: "bookList.boxModule_" + checkboxId,
                    name: name,
                    boxLabel: moduleName,
                    labelSeparator: "",  // 当没有标题时，不要 “:” 号，不要标题分隔
                    inputValue: moduleId,
                    hidden: hidden
                });

                fieldSet.add(checkboxModule); // 这里我是一个 fieldset ，你也可以是一个 form 或者别的东西。
            }
        },

        platformSubmitCheck: function() {
            var size = this.platformStore.getTotalCount();
            var count = 0;
            var name = "feePlatformIds";
            var isFee = App.bookList.frm.form.findField("isFee").getValue();
            if (isFee == 1) {
                for (var i = 0; i < size; i++) {
                    var id = "bookList.boxModule_" + name + '_' + i;
                    var box = Ext.getCmp(id);
                    if (box.getValue()) {
                        count++;
                    }
                }
                if (count == 0) {
                    Ext.Msg.alert('提示', '请选择收费平台');
                    return false;
                }
            }

            var count = 0;
            name = "operatePlatformIds";
            for (var i = 0; i < size; i++) {
                var id = "bookList.boxModule_" + name + '_' + i;
                var box = Ext.getCmp(id);
                if (box.getValue()) {
                    count ++;
                }
            }
            if (count == 0) {
                Ext.Msg.alert('提示', '请选择运营平台');
                return false;
            }

            return true;
        },

        buildCheckGroup: function (values, fieldSetId, checkBoxName, formFieldId, showFieldId, store, typeId) {
            var length = 0;
            if (values) {
                length = values.length;
            }
            var size = store.getTotalCount();
            var fieldSet = Ext.getCmp(fieldSetId);
            fieldSet.removeAll();
            var index = 0;
            for (var i = 0; i < size; i++) {
                var name = store.getAt(i).data.name;
                var children = store.getAt(i).data.children;
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
                    var item = children.items[k];
                    var moduleId = item.id;
                    var moduleName = item.name;
                    var parentId = item.parentId;
                    var idPrefix = checkBoxName + '_' + typeId + '_';

                    checked = false;
                    for (var j = 0; j < length; j++) {
                        if (values[j] == moduleId) {
                            checked = true;
                            break;
                        }
                    }

                    var checkbox = new Ext.form.Checkbox({
                        checked: checked,
                        width: 90,
                        id: idPrefix + index,
                        typeId: typeId,
                        parentId: parentId,
                        idPrefix: idPrefix,
                        formFieldId: formFieldId,
                        showFieldId: showFieldId,
                        name: checkBoxName,
                        totalItems: totalItems,
                        boxLabel: moduleName,
                        labelSeparator: "",  // 当没有标题时，不要 “:” 号，不要标题分隔
                        inputValue: moduleId,
                        listeners: {
                            'check': App.bookList.tagChecked
                        }
                    });

                    subSet.add(checkbox);
                    index ++;
                }

                fieldSet.add(subSet);
            }

            App.bookList.temporaryValues.tagCount = index;
        },

        tagChecked: function(obj, check) {
            var name = '';
            var value = '';
            var count = 0;
            var counter = App.bookList.temporaryValues['tagCounter'+obj.parentId];
            if (check) {
                App.bookList.temporaryValues['tagCounter'+obj.parentId] = counter + 1;
                var tag = App.bookList.parentTagStore.getById(obj.parentId);
                if (tag) {
                    if (!App.bookList.tagCounterCheck(tag, counter)) {
                        obj.setValue(false);
                        return false;
                    }
                }
            } else {
                App.bookList.temporaryValues['tagCounter'+obj.parentId] = counter - 1;
            }

            if (obj.typeId == 3) {
                count = App.bookList.temporaryValues.tagContentCount;
            } else if (obj.typeId == 4) {
                count = App.bookList.temporaryValues.tagSupplyCount;
            }

            if (obj.typeId == 3 && obj.parentId == 4) {
                App.bookList.temporaryValues.formTagStoryName = obj.boxLabel;
                App.bookList.temporaryValues.formTagStoryId = obj.inputValue;
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

            App.bookList.temporaryValues[obj.showFieldId] = name;
            App.bookList.temporaryValues[obj.formFieldId] = value;
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

        tagCounterCheck: function(tag, counter) {
            var limit = tag.get('scope');
            var name = tag.get('name');
            if (limit.length == 1) {
                if (counter + 1 > parseInt(limit)) {
                    Ext.Msg.alert('提示', '['+name+'] 超过数量限制！');
                    return false;
                }
            } else {
                var data = limit.split(',');
                if (counter + 1 > parseInt(data[1])) {
                    Ext.Msg.alert('提示', '['+name+'] 超过数量限制！');
                    return false;
                }
            }

            return true;
        },

        initTagCounter: function(tagContentIds, tagSupplyIds) {
            App.bookList.parentTagStore.each(function (record) {
                App.bookList.temporaryValues['tagCounter' + record.id] = 0;
            });

            if (tagContentIds && tagContentIds.length > 0) {
                App.bookList.changeTagCounter(tagContentIds);
                App.bookList.changeTagCounter(tagSupplyIds);
            }
        },

        changeTagCounter: function(tagIds) {
            var length = tagIds.length;
            for (var i=0;i<length;i++) {
                if (tagIds[i] != '') {
                    var id = parseInt(tagIds[i]);
                    var tag = App.bookList.leafTagStore.getById(id);
                    if (tag) {
                        if (App.bookList.temporaryValues['tagCounter' + tag.get('parentId')]) {
                            var count = App.bookList.temporaryValues['tagCounter' + tag.get('parentId')];
                            App.bookList.temporaryValues['tagCounter' + tag.get('parentId')] = count + 1;
                        } else {
                            App.bookList.temporaryValues['tagCounter' + tag.get('parentId')] = 1;
                        }
                    }
                }
            }
        },

        showRecommendLog: function(bookId) {
            if (App.recommendLogList) {
                var params = {bookId: bookId};
                App.recommendLogList.init(params);
                var tab = App.mainPanel.getItem('recommendLogList');
                App.mainPanel.setActiveTab(tab);
            } else {
                var node = {attributes: {url: "recommendLogList"}, text: "书籍推荐管理", params: {bookId: bookId}};
                App.mainPanel.openTab(node);
            }
        },

        showChapters: function(bookId, chapters, publishChapters) {
            if (chapters == 0) {
                Ext.Msg.alert('提示', '所选书籍还没有添加任何章节');
                return;
            }
            var status = 2;
            if (chapters > publishChapters || publishChapters == 0) {
                status = 0;
            }
            if (App.chapterList) {
                var params = {bookId: bookId, status:status};
                App.chapterList.init(params);
                var tab = App.mainPanel.getItem('chapterList');
                App.mainPanel.setActiveTab(tab);
            } else {
                var node = {attributes: {url: "chapterList"}, text: "章节管理", params: {bookId: bookId, status:status}};
                App.mainPanel.openTab(node);
            }
        },

        emptyTemporaryValues: function () {
            Ext.apply(
                App.bookList.temporaryValues,
                {
                    formTagSexId: '',
                    formTagSexName: '',
                    formTagClassifyId: '',
                    formTagClassifyName: '',
                    formTagStoryId: '',
                    formTagStoryName: '',
                    formTagContentIds: '',
                    formTagContentNames: '',
                    formTagSupplyIds: '',
                    formTagSupplyNames: ''
                }
            );
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.batchOptionStore) {
                this.batchOptionStore = this.getBatchOptionStore();
            };
            if (!this.platformStore) {
                this.platformStore = this.getPlatformStore();
            };
            if (!this.tagContentStore) {
                this.tagContentStore = this.getTagContentStore();
            };
            if (!this.tagSupplyStore) {
                this.tagSupplyStore = this.getTagSupplyStore();
            };
            if (!this.parentTagStore) {
                this.parentTagStore = this.getParentTagStore();
            };
            if (!this.leafTagStore) {
                this.leafTagStore = this.getLeafTagStore();
            };
            if (!this.providerStore) {
                this.providerStore = this.getProviderStore();
            };
            if (!this.frm) {
                this.frm = this.getForm();
            };
            if (!this.dlg) {
                this.dlg = this.getDialog();
            };
            if (!this.tfrm) {
                this.tfrm = this.getTagForm();
            };
            if (!this.tdlg) {
                this.tdlg = this.getTagDialog();
            };
            if (!this.mtfrm) {
                this.mtfrm = this.getMultiAddTagForm();
            };
            if (!this.mtdlg) {
                this.mtdlg = this.getMultiAddTagDialog();
            };

            this.createGrid(id);
        },

        init: function(params) {
        },

        destroy: function() {
            this.store.destroy();
            this.batchOptionStore.destroy();
            this.platformStore.destroy();
            this.tagContentStore.destroy();
            this.tagSupplyStore.destroy();
            this.parentTagStore.destroy();
            this.leafTagStore.destroy();
            this.frm.destroy();
            this.dlg.destroy();
            this.tfrm.destroy();
            this.tdlg.destroy();
            this.mtfrm.destroy();
            this.mtdlg.destroy();
        },

        statusRender: function (value, p, record) {
            var name = '';
            if (value == 0) {
                name = '入库';
            } else if (value == 2) {
                name = '上线';
            } else if (value == 3) {
                name = '下线';
            }
            return String.format(name);
        }

    };
}();
