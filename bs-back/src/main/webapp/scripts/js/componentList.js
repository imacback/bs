App.componentList = function () {
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
                    url: appPath + '/component/list.do'
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
                        },
                        {
                            name: 'title',
                            allowBlank: false
                        },
                        {
                            name: "isUse",
                            type: 'int'
                        },
                        {
                            name: "typeId",
                            type: 'int'
                        },
                        {
                            name: "orderId",
                            type: 'int'
                        },
                        {
                            name: 'typeName',
                            allowBlank: false
                        },
                        {
                            name: "containerId",
                            type: 'int'
                        },
                        {
                            name: 'containerName',
                            allowBlank: false
                        },
                        {
                            name: "dataGroup",
                            type: 'int'
                        },
                        {
                            name: "dataLimit",
                            type: 'int'
                        },
                        {
                            name: 'entryTitle',
                            allowBlank: false
                        },
                        {
                            name: 'entryData',
                            allowBlank: false
                        },
                        {
                            name: "entryDataType",
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
                        },
                        {
                            name: 'orderId',
                            type: 'int'
                        },
                        {
                            name: 'fontColor',
                            type: 'string'
                        },
                        {
                            name: 'icon',
                            type: 'string'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        name: Ext.getCmp("componentList.name").getValue(),
                        //isUse: Ext.getCmp("componentList.isUse").getValue(),
                        containerId: Ext.getCmp("componentList.containerId").getValue()
                    }
                );
            });

            return store;
        },

        getDataStore: function () {
            var dataStore = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/componentData/list.do'
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
                            name: 'componentId',
                            type: 'int'
                        },
                        {
                            name: 'groupId',
                            type: 'int'
                        },
                        {
                            name: 'tabId',
                            type: 'int'
                        },
                        {
                            name: 'dataType',
                            type: 'int'
                        },
                        {
                            name: 'data',
                            allowBlank: false
                        },
                        {
                            name: 'logo',
                            allowBlank: false
                        },
                        {
                            name: 'title',
                            allowBlank: false
                        },
                        {
                            name: 'memo',
                            allowBlank: false
                        },
                        {
                            name: 'orderId',
                            type: 'int'
                        }
                    ]
                })
            });
            dataStore.on('beforeload', function (thiz, options) {
                Ext.apply(thiz.baseParams, {
                    componentId: App.componentList.temporaryValue.componentId,
                    groupId: App.componentList.temporaryValue.groupId
                });
            });
            return dataStore;
        },

        getGroupStore: function () {
            var groupStore = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/componentDataGroup/list.do'
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
                            name: 'componentId',
                            type: 'int'
                        },
                        {
                            name: 'title',
                            allowBlank: false
                        },
                        {
                            name:'orderId',
                            type:'int'
                        }
                    ]
                })
            });
            groupStore.on('beforeload', function (thiz, options) {
                Ext.apply(thiz.baseParams, {
                    componentId: App.componentList.temporaryValue.componentId
                });
            });
            return groupStore;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                fileUpload: true,
                url: appPath + '/component/save.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 80,
                defaultType: 'textfield',
                defaults: {
                    layout: 'form',
                    border: false,
                    anchor: '90%',
                    bodyStyle: 'padding:4px',
                    enableKeyEvents: true
                },
                items: [
                    {
                        xtype: 'hidden',
                        id: 'componentList.form.componentId',
                        name: 'id',
                        value: ''
                    },
                    new Ext.form.ComboBox({
                        name: 'containerId',
                        hiddenName: 'containerId',
                        fieldLabel: '所属页面',
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: { isUse: 1 },
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/container/list.do'
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
                        anchor: '90%',
                        editable: false,
                        allowBlank: false,
                        listeners: {
                            beforequery: function(e) {
                                e.combo.getStore().reload();
                            }
                        }
                    }),
                    new Ext.form.ComboBox({
                        name: 'typeId',
                        hiddenName: 'typeId',
                        fieldLabel: '组件类型',
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: { isUse: 1 },
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/componentType/list.do'
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
                        anchor: '90%',
                        editable: false,
                        allowBlank: false,
                        listeners:{
                            'select': function(combo,records,index){
                                //选中[广告组1、小图导航2、文字导航3、通栏单广告7、通栏双广告8、分类组件9]中一个时,前台标题、标题字色、标题图标为非必填项
                                //下拉框的值
                                var chooseValue = combo.getValue();
                                //前台标题
                                var titleObj = Ext.getCmp("componentList.title");
                                //标题字色
                                var fontColorObj = Ext.getCmp("componentList.fontColor");
                                //图标
                                var imageObj = Ext.getCmp("componentList.componentFileUpload");
                                if(chooseValue=='1' || chooseValue=='2' || chooseValue=='3'
                                    || chooseValue=='7' || chooseValue=='8' || chooseValue=='9'){
                                    titleObj.allowBlank = true;
                                    fontColorObj.allowBlank = true;
                                    imageObj.allowBlank = true;
                                }else{
                                    titleObj.allowBlank = false;
                                    fontColorObj.allowBlank = false;
                                    imageObj.allowBlank = false;
                                }
                            }
                        }
                    }),
                    {
                        name: 'name',
                        fieldLabel: '后台名称',
                        maxLength: 25,
                        allowBlank: false,
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
                        name: 'title',
                        id: 'componentList.title',
                        fieldLabel: '前台标题',
                        maxLength: 25,
                        allowBlank:false,
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
                    {
                        name: 'fontColor',
                        id: 'componentList.fontColor',
                        fieldLabel: '标题字色',
                        allowBlank: false,
                        maxLength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("entryTitle");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        xtype: 'fileuploadfield',
                        fieldLabel: '标题图标',
                        name: 'file',
                        id: 'componentList.componentFileUpload',
                        width: 400,
                        emptyText: '选择图片文件...',
                        regex: /^[^\u4e00-\u9fa5]*$/,
                        regexText: '文件名中不能包含中文',
                        buttonText: '',
                        buttonCfg: {
                            iconCls: 'icon-search'
                        },
                        allowBlank: true
                    },
                    {
                        name: 'entryTitle',
                        fieldLabel: '入口文字',
                        allowBlank: true,
                        maxLength: 32,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("entryData");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'entryDataType',
                        fieldLabel: '入口类型',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['分类', '1'],
                                ['页面', '2'],
                                ['书籍', '3'],
                                ['搜索', '4'],
                                ['url', '5']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {
                        name: 'entryData',
                        fieldLabel: '入口数据ID',
                        allowBlank: true,
                        maxLength: 200,
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
                    {
                        name: 'memo',
                        fieldLabel: '描述',
                        allowBlank: true,
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
                    {
                        name: 'orderId',
                        fieldLabel: '排序',
                        allowBlank: false,
                        maxLength: 5,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数',
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
                                        App.componentList.store.reload();
                                        App.componentList.dlg.hide();
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

        getDataInputForm: function () {
            var dataInputForm = new Ext.form.FormPanel({
                fileUpload: true,
                url: appPath + '/componentData/save.do',
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
                        xtype: 'hidden',
                        name: 'groupId',
                        value: ''
                    },
                    {
                        xtype: 'hidden',
                        name: 'componentId',
                        value: ''
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'dataType',
                        fieldLabel: '数据类型',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['分类', '1'],
                                ['页面', '2'],
                                ['书籍', '3'],
                                ['搜索', '4'],
                                ['url', '5']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        listeners:{
                            'select': function(combo,records,index){
                                //选中"搜索"时,则"数据"项为非必填项
                                var dataObj = Ext.getCmp("componentList.data");
                                if(combo.getValue()=='4'){
                                    dataObj.allowBlank = true;
                                }else{
                                    dataObj.allowBlank = false;
                                }
                            }
                        }
                    }),
                    {
                        name: 'data',
                        id:'componentList.data',
                        fieldLabel: '数据',
                        maxLength: 200,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.dataInputForm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'title',
                        fieldLabel: '标题',
                        maxLength: 25,
                        allowBlank:true,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.dataInputForm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'memo',
                        fieldLabel: '描述',
                        allowBlank:true,
                        maxLength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.dataInputForm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'orderId',
                        fieldLabel: '排序号',
                        maxLength: 5,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.dataInputForm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        xtype: 'fileuploadfield',
                        fieldLabel: '图片',
                        name: 'file',
                        id: 'componentList.fileUpload',
                        width: 400,
                        emptyText: '选择图片文件...',
                        regex: /^[^\u4e00-\u9fa5]*$/,
                        regexText: '文件名中不能包含中文',
                        buttonText: '',
                        buttonCfg: {
                            iconCls: 'icon-search'
                        },
                        allowBlank: true
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            if (this.dataInputForm.form.isValid()) {
                                var data = this.dataInputForm.form.findField("data").getValue();
                                var dataType = this.dataInputForm.form.findField("dataType").getValue();

                                if (!App.componentList.checkData(data, dataType)) {
                                    Ext.Msg.alert('提示:', '数据项的格式错误！');
                                    return;
                                }

                                this.dataInputForm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.componentList.store.reload();
                                        App.componentList.dataStore.reload();
                                        App.componentList.dataInputDialog.hide();
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
                            this.dataInputForm.form.reset();
                            this.dataInputForm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return dataInputForm;
        },

        getGroupInputForm: function () {
            var groupInputForm = new Ext.form.FormPanel({
                fileUpload: false,
                url: appPath + '/componentDataGroup/save.do',
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
                        xtype: 'hidden',
                        name: 'componentId',
                        value: ''
                    },
                    {
                        name: 'title',
                        fieldLabel: '标题',
                        maxLength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.dataInputForm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'orderId',
                        fieldLabel: '排序号',
                        maxLength: 5,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.dataInputForm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            if (this.groupInputForm.form.isValid()) {
                                this.groupInputForm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.componentList.store.reload();
                                        App.componentList.groupStore.reload();
                                        App.componentList.groupInputDialog.hide();
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
                            this.groupInputForm.form.reset();
                            this.groupInputForm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return groupInputForm;
        },

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 400,
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

        getDataDialog: function () {
            var ddlg = new Ext.Window({
                maskDisabled: true,
                width: 900,
                height: 450,
                title: '组件数据管理',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: false,
                items: [this.dataGrid],
                listeners: {
                    scope: this
                }
            }); //dlg
            return ddlg;
        },

        getDataInputDialog: function () {
            var dataInputDialog = new Ext.Window({
                width: 400,
                height: 260,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: false,
                items: [this.dataInputForm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.dataInputForm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.dataInputForm.form.setValues(this.currentFormValues);
                        this.dataInputForm.form.clearInvalid();
                    }
                }
            }); //dlg
            return dataInputDialog;
        },

        getGroupInputDialog: function () {
            var groupInputDialog = new Ext.Window({
                width: 370,
                height: 140,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: false,
                items: [this.groupInputForm],
                listeners: {
                    scope: this,
                    render: function (fp) {
                        this.groupInputForm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function () {
                        this.groupInputForm.form.setValues(this.currentFormValues);
                        this.groupInputForm.form.clearInvalid();
                    }
                }
            }); //dlg
            return groupInputDialog;
        },

        getGroupDialog: function () {
            var gdlg = new Ext.Window({
                maskDisabled: true,
                width: 800,
                height: 400,
                title: '组件数据分组管理',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: false,
                items: [this.groupGrid],
                listeners: {
                    scope: this
                }
            }); //dlg
            return gdlg;
        },

        getDataGrid: function () {
            var sm = new Ext.grid.CheckboxSelectionModel();

            var dataGrid = new Ext.grid.GridPanel({
                loadMask: false,
                tbar: [
                    {
                        xtype: 'button',
                        text: '添加数据',
                        iconCls: 'icon-add',
                        handler: function () {
                            App.componentList.addData();
                        }
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    {
                        xtype: 'button',
                        text: '删除数据',
                        iconCls: 'icon-delete',
                        handler: function () {
                            App.componentList.delData();
                        }
                    }
                ],
                store: this.dataStore,
                sm: sm,
                height: 430,
                region: 'center',
                columns: [sm,
                    {
                        header: "ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "标题",
                        width: 100,
                        sortable: true,
                        dataIndex: 'title',
                        align: 'center'
                    },
                    {
                        header: "描述",
                        width: 100,
                        sortable: true,
                        dataIndex: 'memo',
                        align: 'center'
                    },
                    {
                        header: "数据类型",
                        width: 60,
                        sortable: true,
                        dataIndex: 'dataType',
                        align: 'center',
                        renderer: this.dataTypeRender
                    },
                    {
                        header: "数据",
                        width: 200,
                        sortable: true,
                        dataIndex: 'data',
                        align: 'center'
                    },
                    {
                        header: "排序",
                        width: 40,
                        sortable: true,
                        dataIndex: 'orderId',
                        align: 'center'
                    },
                    {
                        header: "图片",
                        width: 150,
                        sortable: true,
                        dataIndex: 'logo',
                        align: 'center',
                        renderer: App.thumbnailRender
                    },
                    {
                        header: "操作",
                        width: 60,
                        sortable: false,
                        dataIndex: 'id',
                        align: 'center',
                        renderer: optRender
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    store: this.dataStore,
                    pageSize: this.dataStore.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            function optRender(value, p, record) {
                return String.format('<input type="image" src="' + appPath
                    + '/scripts/icons/page_edit.png" title="编辑" onclick="App.componentList.editData(' + value + ')"/>&nbsp;&nbsp;');
            }

            return dataGrid;
        },

        getGroupGrid: function () {
            var sm = new Ext.grid.CheckboxSelectionModel();

            var groupGrid = new Ext.grid.GridPanel({
                loadMask: false,
                tbar: [
                    {
                        xtype: 'button',
                        text: '添加分组',
                        iconCls: 'icon-add',
                        handler: function () {
                            App.componentList.addGroup();
                        }
                    },
                    {
                        xtype: 'tbseparator'
                    },
                    {
                        xtype: 'button',
                        text: '删除分组',
                        iconCls: 'icon-delete',
                        handler: function () {
                            App.componentList.delGroup();
                        }
                    }
                ],
                store: this.groupStore,
                sm: sm,
                height: 430,
                region: 'center',
                columns: [sm,
                    {
                        header: "ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "标题",
                        width: 100,
                        sortable: true,
                        dataIndex: 'title',
                        align: 'center'
                    },
                    {
                        header: "排序",
                        width: 40,
                        sortable: true,
                        dataIndex: 'orderId',
                        align: 'center'
                    },
                    {
                        header: "操作",
                        width: 60,
                        sortable: false,
                        dataIndex: 'id',
                        align: 'center',
                        renderer: optRender
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    store: this.groupStore,
                    pageSize: this.groupStore.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            function optRender(value, p, record) {
                var html = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.componentList.editGroup(' + value + ')"/>';
                html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/table_add.png" title="管理数据" onclick="App.componentList.showData(' + record.data.componentId + ','+ value +')"/>';
                return String.format(html);
            }

            return groupGrid;
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
                            id: 'componentList.name',
                            width: 80
                        },
                        /*
                        {
                            xtype: 'tbseparator'
                        }
                        ,
                        '状态：',
                        new Ext.form.ComboBox({
                            id: 'componentList.isUse',
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
                        */
                        {
                            xtype: 'tbseparator'
                        },
                        '页面ID：',
                        {
                            xtype: 'textfield',
                            id: 'componentList.containerId',
                            width: 80
                        },
                        {xtype: 'tbseparator'},
                        {
                            xtype: 'button',
                            text: '查找',
                            iconCls: 'icon-search',
                            handler: function () {
                                App.componentList.store.load();
                            }
                        }
                    ]
                }
            );

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add,
                        listeners: {
                            'render': function () {
                                if (roleId == 3) {
                                    this.disable();
                                }
                            }
                        }
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
                        header: "所属页面",
                        width: 100,
                        sortable: true,
                        dataIndex: 'containerName',
                        align: 'center'
                    },
                    {
                        header: "后台名称",
                        width: 100,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center'
                    },
                    {
                        header: "分类",
                        width: 80,
                        sortable: true,
                        dataIndex: 'typeName',
                        align: 'center'
                    },
                    {
                        header: "前台标题",
                        width: 100,
                        sortable: true,
                        dataIndex: 'title',
                        align: 'center'
                    },
                    {
                        header: "标题色值",
                        width: 80,
                        sortable: true,
                        dataIndex: 'fontColor',
                        align: 'center'
                    },
                    {
                        header: "排序",
                        width: 60,
                        sortable: true,
                        dataIndex: 'orderId',
                        align: 'center'
                    },
                    {
                        header: "描述",
                        width: 100,
                        sortable: true,
                        dataIndex: 'memo',
                        align: 'center'
                    },
                    {
                        header: "是否可用",
                        width: 60,
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
                var dataGroup = record.data.dataGroup;
                var status = record.data.status;
                var isUse = record.data.isUse;
                var html = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.componentList.edit(' + value + ')"/>';
                if (dataGroup == 1) {
                    html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/table_add.png" title="管理数据" onclick="App.componentList.showData(' + value + ',0)"/>';
                } else {
                    html = html + '&nbsp;&nbsp;' + '<input type="image" src="' + appPath + '/scripts/icons/folder_add.png" title="管理分组" onclick="App.componentList.showGroup(' + value + ')"/>';
                }
                return String.format(html);
            }

            panel.add(this.grid);

        },

        add: function () {
            Ext.apply(App.componentList.currentFormValues, {
                id: '',
                name: "",
                typeId: "",
                title: "",
                entryTitle: "",
                entryData: "",
                entryDataType: "1",
                isRecommendFirst: "0",
                orderId: "",
                memo: "",
                isUse: "1",
                fontColor: "",
                file: ""
            });
            App.componentList.frm.form.findField("typeId").enable();
            App.componentList.frm.form.findField("containerId").enable();
            App.componentList.dlg.setTitle("增加组件");
            App.componentList.dlg.show();
        },

        edit: function () {
            if (App.componentList.grid.getSelectionModel().hasSelection()) {
                App.componentList.dlg.setTitle("编辑组件");
                var rec = App.componentList.grid.getSelectionModel().getSelected();
                Ext.apply(App.componentList.currentFormValues, {
                    id: rec.data.id,
                    containerId: rec.data.containerId,
                    name: rec.data.name,
                    typeId: rec.data.typeId,
                    title: rec.data.title,
                    isRecommendFirst: rec.data.isRecommendFirst,
                    entryTitle: rec.data.entryTitle,
                    entryData: rec.data.entryData,
                    entryDataType: rec.data.entryDataType,
                    orderId: rec.data.orderId,
                    memo: rec.data.memo,
                    isUse: rec.data.isUse,
                    fontColor: rec.data.fontColor
                });
                App.componentList.frm.form.findField("typeId").disable();
                App.componentList.frm.form.findField("containerId").disable();
                App.componentList.dlg.show();

                //组件类型为[广告组1、小图导航2、文字导航3、通栏单广告7、通栏双广告8、分类组件9]中一个时,前台标题、标题字色、
                //标题图标(修改时图标不需要校验)为非必填项
                var typeId = rec.data.typeId;
                //前台标题
                var titleObj = Ext.getCmp("componentList.title");
                //标题字色
                var fontColorObj = Ext.getCmp("componentList.fontColor");
                //图标,修改时图标不需要校验
                var imageObj = Ext.getCmp("componentList.componentFileUpload");
                imageObj.allowBlank = true;
                if(typeId=='1' || typeId=='2' || typeId=='3'
                    || typeId=='7' || typeId=='8' || typeId=='9'){
                    titleObj.allowBlank = true;
                    fontColorObj.allowBlank = true;
                }else{
                    titleObj.allowBlank = false;
                    fontColorObj.allowBlank = false;
                }
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的组件。');
            }
        },

        editData: function () {
            var groupId = App.componentList.temporaryValue.groupId;
            var title = '';
            if (groupId == '' || groupId == 0) {
                title = '组件数据';
            } else {
                title = '组件分组数据';
            }
            if (App.componentList.dataGrid.getSelectionModel().hasSelection()) {
                App.componentList.dataGrid.setTitle("编辑"+title);
                var rec = App.componentList.dataGrid.getSelectionModel().getSelected();
                Ext.apply(App.componentList.currentFormValues, {
                    id: rec.data.id,
                    componentId: App.componentList.temporaryValue.componentId,
                    groupId: App.componentList.temporaryValue.groupId,
                    tabId: rec.data.tabId,
                    dataType: rec.data.dataType,
                    data: rec.data.data,
                    logo: rec.data.logo,
                    title: rec.data.title,
                    memo: rec.data.memo,
                    orderId: rec.data.orderId
                });
                App.componentList.dataInputDialog.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的'+title);
            }
        },

        editGroup: function () {
            if (App.componentList.groupGrid.getSelectionModel().hasSelection()) {
                App.componentList.groupGrid.setTitle("编辑组件数据");
                var rec = App.componentList.groupGrid.getSelectionModel().getSelected();
                Ext.apply(App.componentList.currentFormValues, {
                    id: rec.data.id,
                    componentId: App.componentList.temporaryValue.componentId,
                    title: rec.data.title,
                    orderId: rec.data.orderId
                });
                App.componentList.groupInputDialog.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的组件分组。');
            }
        },

        showData: function (componentId, groupId) {
            Ext.apply(App.componentList.temporaryValue, {
                componentId: componentId,
                groupId: groupId
            });
            this.dataStore.load();
            App.componentList.ddlg.show();
        },

        showGroup: function (componentId) {
            Ext.apply(App.componentList.temporaryValue, {
                componentId: componentId
            });
            this.groupStore.reload();
            App.componentList.gdlg.show();
        },

        addData: function () {
            var groupId = App.componentList.temporaryValue.groupId;
            var title = '';
            if (groupId == '' || groupId == 0) {
                title = '添加组件数据';
            } else {
                title = '添加组件分组数据';
            }
            Ext.apply(App.componentList.currentFormValues, {
                id: '',
                componentId: App.componentList.temporaryValue.componentId,
                groupId: App.componentList.temporaryValue.groupId,
                tabId: "",
                data: "",
                dataType: "",
                logo: "",
                title: "",
                memo: "",
                orderId: ""
            });
            App.componentList.dataInputDialog.setTitle(title);
            App.componentList.dataInputDialog.show();
        },

        addGroup: function () {
            Ext.apply(App.componentList.currentFormValues, {
                id: '',
                componentId: App.componentList.temporaryValue.componentId,
                title: "",
                orderId: ""
            });
            App.componentList.groupInputDialog.setTitle('添加分组');
            App.componentList.groupInputDialog.show();
        },

        delData: function () {
            if (App.componentList.dataGrid.getSelectionModel().hasSelection()) {
                var recs = App.componentList.dataGrid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    words += data.id + '<br>';
                }
                Ext.Msg.confirm('删除数据', '确定删除以下数据？<br><font color="red">' + words + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/componentData/del.do',
                                params: {
                                    ids: ids.toString(),
                                    componentId:App.componentList.temporaryValue.componentId
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.componentList.store.reload();
                                        App.componentList.dataStore.load();
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
                Ext.Msg.alert('信息', '请选择要删除的数据！');
            }
        },

        //校验"数据"项
        checkData: function(data, dataType) {
            //1:分类 2:页面 3:书籍 4:搜索 5:url
            if (dataType == 1 || dataType == 2 || dataType == 3) {
                if (!App.isNumber(data)) {
                    return false;
                }
            }

            return true;
        },

        delGroup: function () {
            if (App.componentList.groupGrid.getSelectionModel().hasSelection()) {
                var recs = App.componentList.groupGrid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    words += data.title + '<br>';
                }
                Ext.Msg.confirm('删除分组', '确定删除以下分组？<br><font color="red">' + words + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/componentDataGroup/del.do',
                                params: {
                                    ids: ids.toString(),
                                    componentId:App.componentList.temporaryValue.componentId
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.componentList.store.reload();
                                        App.componentList.groupStore.load();
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
                Ext.Msg.alert('信息', '请选择要删除的分组！');
            }
        },

        publish: function (componentId) {
            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function (btn) {
                if (btn == 'yes') {
                    var requestConfig = {
                        url: appPath + '/component/publish.do?componentId=' + componentId,
                        callback: function (o, s, r) {
                            var result = Ext.util.JSON.decode(r.responseText);
                            if (result.success == 'true') {
                                App.componentList.store.reload();
                            }
                            Ext.Msg.alert('提示', result.info);
                        }
                    }
                    Ext.Ajax.request(requestConfig);
                }
            });
        },

        reload: function() {
            this.groupStore.reload();
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };

            if (!this.dataStore) {
                this.dataStore = this.getDataStore();
            };

            if (!this.groupStore) {
                this.groupStore = this.getGroupStore();
            };

            if (!this.dataGrid) {
                this.dataGrid = this.getDataGrid();
            };

            if (!this.groupGrid) {
                this.groupGrid = this.getGroupGrid();
            };

            if (!this.frm) {
                this.frm = this.getForm();
            }
            if (!this.dataInputForm) {
                this.dataInputForm = this.getDataInputForm();
            }
            if (!this.groupInputForm) {
                this.groupInputForm = this.getGroupInputForm();
            }

            if (!this.dlg) {
                this.dlg = this.getDialog();
            }
            if (!this.ddlg) {
                this.ddlg = this.getDataDialog();
            }
            if (!this.gdlg) {
                this.gdlg = this.getGroupDialog();
            }
            if (!this.dataInputDialog) {
                this.dataInputDialog = this.getDataInputDialog()
            }; //if(!this.dlg)
            if (!this.groupInputDialog) {
                this.groupInputDialog = this.getGroupInputDialog()
            };

            this.createGrid(id);

        },

        dataTypeRender: function (value, p, record) {
            var name;
            if (value == 1) {
                name = '分类';
            } else if (value == 2) {
                name = '页面';
            } else if (value == 3) {
                name = '书籍';
            } else if (value == 4) {
                name = '搜索';
            } else if (value == 5) {
                name = 'url';
            }
            return String.format(name);
        },

        init: function (params) {
            //Ext.getCmp("componentList.isUse").setValue(params.isUse);
            Ext.getCmp("componentList.containerId").setValue(params.containerId);

            this.store.reload();
        },

        destroy: function() {
            this.store.destroy();
            this.dataStore.destroy();
            this.groupStore.destroy();
            this.frm.destroy();
            this.dlg.destroy();
            this.dataInputForm.destroy();
            this.ddlg.destroy();
            this.groupInputForm.destroy();
            this.gdlg.destroy();
            this.dataInputDialog.destroy();
            this.groupInputDialog.destroy();
        }
    };
}
();
