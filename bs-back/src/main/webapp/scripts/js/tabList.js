App.tabList = function () {
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
                    url: appPath + '/clientTab/list.do'
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
                            name: 'url',
                            allowBlank: false
                        },
                        {
                            name: 'orderId',
                            allowBlank: false
                        } ,
                        ,
                        {
                            name: 'status',
                            allowBlank: false 
                            
                        } 
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/clientTab/changeOrder.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 60,
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
                        maxlength: 25,
                        regex: /^[\u4e00-\u9fa5]{2,4}$/,
                        regexText: '只能输入汉字',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("name");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'url',
                        fieldLabel: 'url',
                        maxlength: 25,
                        vtype: 'url',
                        vtypeText: '输入正确的url地址',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("url");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        name: 'orderId',
                        fieldLabel: '序号',
                        maxlength: 2,
                        regex: /^[0-9]/,
                        regexText: '输入数字',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("orderId");
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
                                        App.tabList.store.reload();
                                        App.tabList.dlg.hide();
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
                width: 380,
                height: 170,
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
                store: this.store,
                tbar: [
                       {
                           xtype: 'button',
                           text: '发布',
                           iconCls: 'icon-add',
                           handler: this.release
                       }
                   ],
                sm: sm,
                columns: [ {
                    header: "ID",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center',
                    hideable: false
                }, {
                    header: "名称",
                    width: 80,
                    sortable: true,
                    dataIndex: 'name',
                    align: 'center'
                }, {
                    header: "URL",
                    width: 350,
                    sortable: true,
                    dataIndex: 'url',
                    align: 'center'
                }, {
                    header: "序号",
                    width: 60,
                    sortable: true,
                    dataIndex: 'orderId',
                    align: 'center'
                }, {
                    header: "操作",
                    width: 60,
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

            function optRender(value, p, record) {
            	var s = record.data.isAct ;
            	var st    = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="image" src="' + appPath + '/scripts/icons/page_go.png" title="发布" onclick="App.tabList.status_up(' + value + ')"/>';
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.tabList.edit(' + value + ')"/>' );
            }

            panel.add(this.grid);

        },
        edit: function () {
            if (App.tabList.grid.getSelectionModel().hasSelection()) {
                App.tabList.dlg.setTitle("编辑");
                var rec = App.tabList.grid.getSelectionModel().getSelected();
                Ext.apply(App.tabList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    url: rec.data.url,
                    orderId: rec.data.orderId 
                });
                App.tabList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的Tab信息。');
            }
        },
        status_up:function(){
        	 if (App.tabList.grid.getSelectionModel().hasSelection()) {
        		 var rec = App.tabList.grid.getSelectionModel().getSelected();
        		 var ids = rec.data.id;
        	     Ext.Msg.confirm('取消发布', '确定取消发布',
                         function (btn) {
                             if (btn == 'yes') {
                                 Ext.Ajax.request({
                                     method: 'post',
                                     url: appPath + '/clientTab/release.do',
                                     params: {
                                         id: ids,
                                         status:0
                                     },
                                     success: function (resp, opts) {
                                         var result = Ext.util.JSON.decode(resp.responseText);
                                         var info = result.info;
                                         if (result.success == 'true') {
                                             Ext.Msg.alert('信息', info);
                                             App.tabList.store.load();
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
        		 
        		 
        	 }
        },
        release:function(){
        	   
        	     Ext.Msg.confirm('发布', '确定发布',
                         function (btn) {
                             if (btn == 'yes') {
                                 Ext.Ajax.request({
                                     method: 'post',
                                     url: appPath + '/clientTab/release.do',
                                     params: {
                                         id: 0,
                                         status:1
                                     },
                                     success: function (resp, opts) {
                                         var result = Ext.util.JSON.decode(resp.responseText);
                                         var info = result.info;
                                         if (result.success == 'true') {
                                             Ext.Msg.alert('信息', info);
                                             App.tabList.store.load();
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
