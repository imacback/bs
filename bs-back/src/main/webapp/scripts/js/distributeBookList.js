App.distributeBookList = function () {
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
                    url: appPath + '/distributeBook/list.do'
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
                            name: 'bookName',
                            allowBlank: false
                        },
                        {
                            name: 'author',
                            allowBlank: false
                        },
                         {
                            name: 'distributeName',
                             allowBlank: false
                         },
                        {
                            name: 'bookId',
                            type: 'int'
                        },
                        {
                            name: 'disId',
                            type: 'int'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
             var pageBar = Ext.getCmp('distributeBookList.pageBar');
                   if (pageBar && pageBar.cursor > 0) {
              }
                 Ext.apply(thiz.baseParams, {
                 bookName:"",
                 bookId:Ext.getCmp("distributeBookList.bookId").getValue(),
                 disId:Ext.getCmp("distributeBookList.disId").getValue()
               });
            });
            return store;
        },
        getDisStore: function () {
            var disStore = new Ext.data.Store({
                   autoLoad: {},
                  proxy: new Ext.data.HttpProxy(
                  {
                      url : appPath + '/distribute/query.do'
                  }),
                  remoteSort: false,
                  reader: new Ext.data.JsonReader({
                      totalProperty: 'totalItems',
                      root: 'result',
                      idProperty: 'id',
                      fields: ['id', 'name']
                  })
             });
               disStore.on('beforeload', function (thiz, options) {
                              Ext.apply(thiz.baseParams, {
                              start:1
                            });
                         });
            return disStore;
         },
        getDisStoreAll: function () {
                var disStoreAll = new Ext.data.Store({
                      autoLoad: {},
                      proxy: new Ext.data.HttpProxy(
                      {
                          url : appPath + '/distribute/query.do'
                      }),
                      remoteSort: false,
                      reader: new Ext.data.JsonReader({
                          totalProperty: 'totalItems',
                          root: 'result',
                          idProperty: 'id',
                          fields: ['id', 'name']
                      })
                 });
                  disStoreAll.on('beforeload', function (thiz, options) {
                           Ext.apply(thiz.baseParams, {
                           start:""
                         });
                     });
                 disStoreAll.on('load',function () {
                      this.add(allRecord);
                 });
                return disStoreAll;
             },
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                 fileUpload: true,
                url: appPath + '/distributeBook/save.do',
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
                        value: ''
                    },
                    {
                        name: 'bookId',
                        id:"bookid_nane",
                        fieldLabel: '书籍编号（多个以";"分隔）',
                        maxLength: 500,
                        regex: /^[0-9;.]+$/,
                        allowBlank: true,
                        regexText:'请输入大于0整数'
                    },
                    new Ext.form.ComboBox({
                        hiddenName : 'disId',
                        fieldLabel : '推广渠道',
                        store:this.disStore,
                        mode : 'local',
                        displayField : 'name',
                        valueField : 'id',
                        triggerAction : 'all',
                        editable : false
                    }),
                    {
                         xtype: 'fileuploadfield',
                         fieldLabel: 'Excel',
                         name: 'file',
                         id: 'book_file',
                         emptyText: '选择上传文件...',
                         regex: /^[^\u4e00-\u9fa5]*$/,
                         regexText: '文件名中不能包含中文',
                         buttonText: '',
                         buttonCfg: {
                             iconCls: 'icon-search'
                         },
                         allowBlank: true
                     }

                ],
                buttons: [
                    {
                        text: '保存',
                        iconCls: 'icon-save',
                        scope: this,
                        handler: function () {
                            var  id =Ext.getCmp("bookid_nane").getValue();
                            var  fle = Ext.getCmp("book_file").getValue();
                            if(id =="" && fle==""){
                                Ext.Msg.alert('提示:', "书籍ID、上传文件不能同时为空!");
                                return ;
                            }

                            if (this.frm.form.isValid()) {
                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.distributeBookList.store.reload();
                                        App.distributeBookList.dlg.hide();
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
                             if(this.frm.form.findField("id").getValue()==""){
                                this.frm.form.reset();
                                this.frm.form.clearInvalid();
                                }else{
                                this.frm.form.findField("disId").setValue("");
                                }
                        }
                    }
                ] //buttons
            }); //FormPanel

            return frm;
        },

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 600,
                height: 230,
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
                    '书籍ID：',
                    {
                        xtype: 'textfield',
                        id: 'distributeBookList.bookId',
                        width: 80
                    } ,
                    {
                        xtype: 'tbseparator'
                    },
                    '推广渠道',
                    new Ext.form.ComboBox({
                         id: 'distributeBookList.disId',
                         name:'distributeBookList.name',
                         store:this.disStoreAll,
                        // mode : 'local',
                        displayField : 'name',
                        valueField : 'id',
                        triggerAction : 'all',
                        editable : false
                     }),
                    {
                        xtype: 'tbseparator'
                    }, {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {

                            App.distributeBookList.store.load();
                        }
                    }]
            });
             var sm = new Ext.grid.CheckboxSelectionModel();
            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                store: this.store,
                tbar: [
                       {
                           xtype: 'button',
                           text: '添加书单',
                           iconCls: 'icon-add',
                           handler: this.add
                       }
                   ],
                sm: sm,
                columns: [ {
                    header: "渠道名称",
                    width: 150,
                    sortable: true,
                    dataIndex: 'distributeName',
                    align: 'center'
                },{
                   header: "书籍ID",
                   width: 150,
                   sortable: true,
                   dataIndex: 'bookId',
                   align: 'center'
               },{
                  header: "书籍名称",
                  width: 150,
                  sortable: true,
                  dataIndex: 'bookName',
                  align: 'center'
              },{
                   header: "作者",
                   width: 150,
                   sortable: true,
                   dataIndex: 'author',
                   align: 'center'
               },
               {
                    header: "操作",
                    width: 150,
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
                    id: 'distributeBookList.pageBar',
                    store: this.store,
                    pageSize: this.store.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            function optRender(value, p, record) {
                            var ca = '<input type="image"   src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.distributeBookList.edit('+ value + ')"/>'
                            +'<input type="image" src="' + appPath + '/scripts/icons/delete.png" title="删除" onclick="App.distributeBookList.del('+ record.data.id +','+ record.data.disId +')"/>';
                            return String.format(ca );
             };

            panel.add(this.grid);

        },
        edit: function () {
             App.distributeBookList.frm.form.reset();
            if (App.distributeBookList.grid.getSelectionModel().hasSelection()) {
                App.distributeBookList.dlg.setTitle("编辑");
                var rec = App.distributeBookList.grid.getSelectionModel().getSelected();
                Ext.apply(App.distributeBookList.currentFormValues, {
                    id: rec.data.id,
                    disId: rec.data.disId,
                    bookIds: rec.data.bookId,
                    bookId:rec.data.bookId

                });
                App.distributeBookList.frm.form.findField("file").setReadOnly(true);
                App.distributeBookList.frm.form.findField("bookId").setReadOnly(true);
                App.distributeBookList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的渠道信息。');
            }
        },
        del:function(id,disId){
            Ext.Msg.confirm('信息提示','确定要删除?',function(btn){
            if(btn=='yes'){
                Ext.Ajax.request({
                    url: appPath + '/distributeBook/delete.do',
                    params: {id:id,disId:disId},
                    method: 'POST',
                    success: function (resp, opts) {
                        var result = Ext.util.JSON.decode(resp.responseText);
                        var info = result.info;
                        if (result.success == 'true') {
                            Ext.Msg.alert('信息', info);
                        } else {
                            Ext.Msg.alert('信息', info);
                        }
                        //刷新,重新加载列表信息
                        App.distributeBookList.store.reload();
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
        add: function () {
           App.distributeBookList.frm.form.reset();
           Ext.apply(App.distributeBookList.currentFormValues, {
                           id: '',
                           disId: '',
                           bookId: '',
                           bookIds: '',
                           file:null
                       });
                       App.distributeBookList.dlg.setTitle("增加书单");
                       App.distributeBookList.frm.form.findField("file").setReadOnly(false);
                       App.distributeBookList.frm.form.findField("bookId").setReadOnly(false);
                       App.distributeBookList.dlg.show();
        },


        render: function (id) {
        	if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.disStore) {
               this.disStore = this.getDisStore();
            } ;
            if(!this.disStoreAll){
                this.disStoreAll=this.getDisStoreAll();
            };

            if (!this.frm) {
                this.frm = this.getForm();
            }
            ; //if(!this.frm)


            if (!this.dlg) {
                this.dlg = this.getDialog();
            }
            this.createGrid(id);

        },

        init: function(params) {
              //this.disStoreAll.load();
              if(params!=null){

                Ext.getCmp("distributeBookList.bookId").setValue(),
                Ext.getCmp("distributeBookList.disId").setValue(params.disId);
                   this.store.load();
                   this.disStoreAll.load();
                   this.disStore.load();
                   this.store.proxy.on('load',function(){
                   var  value  =Ext.getCmp("distributeBookList.disId").getValue();
                   if( params!=""){
                     Ext.getCmp("distributeBookList.disId").setValue(params.disId);
                   }else{
                     Ext.getCmp("distributeBookList.disId").setValue(value);
                   }
                   params="";

                  });


              }else{
               this.disStoreAll.reload();
               this.disStore.reload();
               }

        },
        destroy: function() {
             this.store.destroy();
             this.frm.destroy();
             this.dlg.destroy();
        }
    };
}();
