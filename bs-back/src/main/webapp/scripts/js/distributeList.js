App.distributeList = function () {
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
                    url: appPath + '/distribute/list.do'
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
                            name: 'userName',
                            allowBlank: false
                        },
                        {
                            name: 'status',
                            type: 'int'
                        },
                         {
                              name: 'isCategory',
                              type: 'int'
                          },
                         {
                             name: 'count',
                             type: 'int'
                         },
                         {
                             name: 'key',
                            allowBlank: false
                         }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(thiz.baseParams, {
                    name:Ext.getCmp("distribute.disName").getValue(),
                    status:Ext.getCmp("distribute.status").getValue(),
                    isCategory: Ext.getCmp("distribute.isCategory").getValue()
               });
            });
            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                url: appPath + '/distribute/save.do',
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
                        fieldLabel: '渠道名称',
                        maxLengthText:"渠道名称不能超过40个字符!",
                        maxLength: 30
                    },
                    {
                        name: 'key',
                        fieldLabel: '私钥',
                        allowBlank: true,
                        maxLength: 40
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
                     }), new Ext.form.ComboBox({
                        hiddenName: 'isCategory',
                        fieldLabel: '是否配置分类',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [['是', '1'], ['否', '0']]
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
                                        App.distributeList.store.reload();
                                        App.distributeList.dlg.hide();
                                       if (App.distributeBookList) {
                                          var params = {disId: ''};
                                          App.distributeBookList.init(params);

                                          App.categoryList.init();
                                       }
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
                            this.frm.form.findField("name").setValue("");
                            }

                        }
                    }
                ] //buttons
            }); //FormPanel

            return frm;
        },

        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
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
                  var searchBar = new Ext.Toolbar({
                        renderTo: Ext.grid.GridPanel.tbar,
                        items: [
                            '状态：',
                             new Ext.form.ComboBox({
                                id: 'distribute.status',
                                fieldLabel: '状态',
                                store: new Ext.data.SimpleStore({
                                    fields: ['text', 'value'],
                                    data: [['上线', '1'], ['下线', '0'],['全部','']]
                                }),
                                 mode: 'local',
                                displayField: 'text',
                                valueField: 'value',
                                triggerAction: 'all',
                                editable: false
                            }),'是否配置分类',
                             new Ext.form.ComboBox({
                                id: 'distribute.isCategory',
                                fieldLabel: '是否配置分类',
                                store: new Ext.data.SimpleStore({
                                    fields: ['text', 'value'],
                                    data: [['是', '1'], ['否', '0'],['全部','']]
                                }),
                                 mode: 'local',
                                displayField: 'text',
                                valueField: 'value',
                                triggerAction: 'all',
                                editable: false
                            }),
                            {
                                xtype: 'tbseparator'
                            },
                            '渠道名称：',
                            {
                                xtype: 'textfield',
                                id: 'distribute.disName',
                                width: 80
                            }
                            ,
                            {
                                xtype: 'tbseparator'
                            }, {
                                xtype: 'button',
                                text: '查找',
                                iconCls: 'icon-search',
                                handler: function () {
                                    App.distributeList.store.load();
                                }
                            }]
                    });
            var panel = Ext.getCmp(id);
            panel.body.dom.innerHTML = "";
             var sm = new Ext.grid.CheckboxSelectionModel();
            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                store: this.store,
                tbar: [
                       {
                           xtype: 'button',
                           text: '添加',
                           iconCls: 'icon-add',
                           handler: this.add
                       }
                   ],
                sm: sm,
                columns: [ sm, {
                    header: "ID",
                    width: 100,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center'
                }, {
                    header: "名称",
                    width: 200,
                    sortable: true,
                    dataIndex: 'name',
                    align: 'center'
                }, {
                     header: "状态",
                     width: 100,
                     sortable: true,
                     dataIndex: 'status',
                     renderer: App.statusRender,
                     align: 'center'
                     },{
                      header: "是否配置分类",
                      width: 120,
                      sortable: true,
                      dataIndex: 'isCategory',
                      renderer: App.statusRender,
                      align: 'center'
                      },{
                       header: "书籍数量",
                       width: 100,
                       sortable: true,
                       dataIndex: 'count',
                       align: 'center'
                       }, {
                         header: "创建者",
                         width: 200,
                         sortable: true,
                         dataIndex: 'userName',
                         align: 'center'
                     }, {
                      header: "操作",
                      width: 150,
                      sortable: false,
                      dataIndex: 'id',
                      align: 'center',
                      renderer: optRender
                  } ],
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
                var ca = '<input type="image"   src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.distributeList.edit('+ value + ')"/>'
                +'<input type="image" src="' + appPath + '/scripts/icons/book_open.png" title="查看" onclick="App.distributeList.showChapters(' + value + ','+ record.data.id +','+record.data.count+')"/>';
                return String.format(ca );
            };

            panel.add(this.grid);

        },
        edit: function () {
            if (App.distributeList.grid.getSelectionModel().hasSelection()) {
                App.distributeList.dlg.setTitle("编辑");
                var rec = App.distributeList.grid.getSelectionModel().getSelected();
                Ext.apply(App.distributeList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    status:rec.data.status,
                    isCategory:rec.data.isCategory,
                    key:rec.data.key
                });
                App.distributeList.frm.form.findField("key").setReadOnly(true);
                App.distributeList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的渠道信息。');
            }
        },
        add: function () {
           Ext.apply(App.distributeList.currentFormValues, {
                   id: '',
                   name: '',
                   key: '',
                   isCategory:1,
                   status: 1
               });
                App.distributeList.dlg.setTitle("增加资源");
                App.distributeList.frm.form.findField("key").setReadOnly(false);
                App.distributeList.dlg.show();
        },
        del:function(){
         if (App.distributeList.grid.getSelectionModel().hasSelection()) {
                App.distributeList.dlg.setTitle("编辑");
                var rec = App.distributeList.grid.getSelectionModel().getSelected();
                var ids = 0;
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids = data.id;
                }
                 Ext.Msg.confirm('删除', '确定删除渠道',
                     function (btn) {
                         if (btn == 'yes') {
                             Ext.Ajax.request({
                                 method: 'post',
                                 url: appPath + '/distribute/delete.do',
                                 params: {
                                     id: ids
                                 },
                                 success: function (resp, opts) {
                                     var result = Ext.util.JSON.decode(resp.responseText);
                                     var info = result.info;
                                     if (result.success == 'true') {
                                         Ext.Msg.alert('信息', info);
                                         App.distributeList.store.load();
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
                Ext.Msg.alert('信息', '请选择要删除的渠道信息。');
            }


        	 
        },

        showChapters:function(v ,id ,count){
            if(count==0){
                Ext.Msg.alert('信息', '没有添加书籍信息。');
                return ;
            }else{
                if (App.distributeBookList) {
                    var params = {disId: id};
                    App.distributeBookList.init(params);
                    var tab = App.mainPanel.getItem('distributeBookList');
                    App.mainPanel.setActiveTab(tab);
                } else {
                    var node = {attributes: {url: "distributeBookList"}, text: "书单管理", params: {disId: id}};
                    App.mainPanel.openTab(node);
                }
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
