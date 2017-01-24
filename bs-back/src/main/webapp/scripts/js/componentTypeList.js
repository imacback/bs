App.componentTypeList = function () {
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
                    url: appPath + '/componentType/list.do'
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
                            name: 'dataLimit',
                            type: 'int'
                        },
                        {
                            name: 'dataGroup',
                            type: 'int'
                        },
                        {
                            name: 'id',
                            type: 'int'
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
                        name: Ext.getCmp("componentTypeList.name").getValue()
                    }
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/componentType/save.do',
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
                        fieldLabel: '名称',
                        maxLength: 20,
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
                        name: 'dataLimit',
                        fieldLabel: '数据数量上限',
                        maxLength: 2,
                        regex: /^[0-9]+$/,
                        regexText: '输入数字',
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
                        name: 'dataGroup',
                        fieldLabel: '数据分组数量',
                        maxLength: 1,
                        regex: /^[0-9]+$/,
                        regexText: '输入数字',
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
                        name: 'memo',
                        fieldLabel: '描述',
                        maxLength: 25,
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
                                        App.componentTypeList.store.reload();
                                        App.componentTypeList.dlg.hide();
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
                height: 240,
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

            var editor = new Ext.ux.grid.RowEditor({
                saveText: '更新',
                cancelText: '取消',
                errorSummary:false,
                clicksToEdit:2,//
                listeners : {
                    beforeedit:function(roweditor){
                        roweditor.stopMonitoring();
                    },
                    afteredit : function(roweditor,record,recordBeforeEdited,index) {
                        App.componentTypeList.store.commitChanges();
                        var requestConfig = {
                            url :  appPath + '/componentType/save.do',
                            params : {
                                id:recordBeforeEdited.data.id,
                                dataLimit:recordBeforeEdited.data.dataLimit
                            },
                            callback:function(o, s, r) {
                                var result = Ext.util.JSON.decode(r.responseText);
                                if (!result.success) {
                                    App.componentTypeList.store.reload();
                                }
                                Ext.Msg.alert('提示',result.info);
                            }
                        }
                        Ext.Ajax.request(requestConfig);
                    },
                    canceledit:function(){
                        App.componentTypeList.store.rejectChanges();
                    }
                }
            });

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    '名称：',
                    {
                        xtype: 'textfield',
                        id: 'componentTypeList.name',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.componentTypeList.store.load();
                        }
                    }
                ],
                plugins: [editor],
                store: this.store,
                columns: [
                    {
                        header: "序号",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "名称",
                        width: 100,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center'
                    },
                    {
                        header: "数据数量上限",
                        width: 100,
                        sortable: true,
                        dataIndex: 'dataLimit',
                        align: 'center',
                        editor: {
                            xtype: 'textfield',
                            allowBlank: false,
                            regex:/^\d*$/,
                            regexText:'请输入大于0整数'
                        }
                    },
                    {
                        header: "数据分组数量",
                        width: 100,
                        sortable: true,
                        dataIndex: 'dataGroup',
                        align: 'center'
                    },
                    {
                        header: "描述",
                        width: 150,
                        sortable: true,
                        dataIndex: 'memo',
                        align: 'center'
                    }
                ],
                bbar: new Ext.PagingToolbar({
                    store: this.store,
                    pageSize: this.store.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            function optRender(value, p, record) {
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.componentTypeList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        add: function () {
            Ext.apply(App.componentTypeList.currentFormValues, {
                id: '',
                name: "",
                dataLimit: "",
                dataGroup: "",
                memo: ""
            });
            App.componentTypeList.dlg.setTitle("增加组件分类");
            App.componentTypeList.dlg.show();
        },

        edit: function () {
            if (App.componentTypeList.grid.getSelectionModel().hasSelection()) {
                App.componentTypeList.dlg.setTitle("编辑组件分类");
                var rec = App.componentTypeList.grid.getSelectionModel().getSelected();
                Ext.apply(App.componentTypeList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    dataLimit: rec.data.dataLimit,
                    dataGroup: rec.data.dataGroup,
                    memo: rec.data.memo
                });
                App.componentTypeList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的组件分类。');
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

        destroy: function() {}
    };
}();
