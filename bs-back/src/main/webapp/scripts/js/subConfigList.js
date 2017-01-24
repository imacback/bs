App.subConfigList = function () {
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
                    url: appPath + '/subConfig/list.do'
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
                        },{
                            name: 'dayNum',
                            type: 'int'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
            });
            return store;
        },

        //编辑订阅配置信息的Form表单
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
            	url: appPath + '/subConfig/edit.do',
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
                        name: 'dayNum',
                        fieldLabel: '调整前',
                        disabled:true
                    },
                    {
                        name: 'newDayNum',
                        id:'newDayNum',
                        fieldLabel: '调整后',
                        maxLength: 10,
                        regex:/^\d*$/,
                        regexText:'请输入大于0整数'
                    }
                ],
                //items
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function (){
                            if (this.frm.form.isValid()) {
                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.subConfigList.dlg.hide();

                                        //刷新,重新加载列表信息
                                        App.subConfigList.store.reload();
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
                            Ext.getCmp("newDayNum").setValue('');
                        }
                    }
                ] //buttons
            }); //FormPanel

            return frm;
        },
        
        //订阅配置的窗口
        getDialog: function () {
            var dlg = new Ext.Window({
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

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,

                /*
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add
                    }
                ],
                */

                store: this.store,
                sm: sm,
                //columns: [sm, {
                columns: [{
                    header: "ID",
                    width: 80,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center'
                }, {
                    header: "自动订购有效时间(天)",
                    width: 150,
                    sortable: true,
                    dataIndex: 'dayNum',
                    align: 'center'
                },{
                    header: "操作",
                    width: 100,
                    sortable: false,
                    dataIndex: 'id',
                    align: 'center',
                    renderer: optRender
                }],
                bbar: new Ext.PagingToolbar({
                    store: this.store,
                    pageSize: this.store.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            //编辑操作
            function optRender(value, p, record) {
            	var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.subConfigList.edit(' + value + ')"/>';
                return String.format(editStr);
            }
            panel.add(this.grid);

        },
        
        //订阅配置信息编辑
        edit: function() {
            if (App.subConfigList.grid.getSelectionModel().hasSelection()) {
                App.subConfigList.dlg.setTitle("编辑订阅配置");
                var rec = App.subConfigList.grid.getSelectionModel().getSelected();

                //编辑窗口信息的回显
                Ext.apply(App.subConfigList.currentFormValues, {
                    id: rec.data.id,
                    dayNum: rec.data.dayNum
                });
                App.subConfigList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的订阅配置。');
            }
        },

        /*
        add: function () {
            Ext.apply(App.subConfigList.currentFormValues, {
                id: "",
                dayNum: ""
            });
            App.subConfigList.dlg.setTitle("增加订阅配置");
            App.subConfigList.dlg.show();
        },
        */

        render: function (id){
            if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.frm) {
                this.frm = this.getForm();
            };
            if (!this.dlg) {
                this.dlg = this.getDialog();
            };
            this.createGrid(id);
        },

        destroy: function() {
            this.store.destroy();
            this.frm.destroy();
            this.dlg.destroy();
        }
    };
}();
