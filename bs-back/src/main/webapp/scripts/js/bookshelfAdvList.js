
/*书架广告*/
App.bookshelfAdvList = function() {
	return {

		currentFormValues : {},

		getStore : function() {
			var store = new Ext.data.Store({
				baseParams : {
					start : App.start_page_default,
					limit : App.limit_page_default
				},
				autoLoad : {},
				proxy : new Ext.data.HttpProxy({
					url : appPath + '/advShelf/list.do'
				}),
				remoteSort : false,
				reader : new Ext.data.JsonReader({
					totalProperty : 'totalItems',
					root : 'result',
					idProperty : 'id',
					fields : [ {
						name : 'id',
						type : 'int'
					}, {
                        name : 'isUseDitch',
                        type : 'int'
                    }, {
                         name : 'status',
                         type : 'int'
                    } ,{
                           name : 'typeId',
                           type : 'int'
                    } ,{
                         name : 'ditchIds',
                         allowBlank : false
                    },{
						name : 'platformId',
						type : 'int' 
					}, {
						name : 'name',
						allowBlank : false
					}, {
						name : 'status',
						type : 'int' 
					}, {
						name : "url1",
						allowBlank : false
					}, {
						name : 'content1',
						allowBlank : false
					}, {
                        name : "url2",
                        allowBlank : false
                    }, {
                        name : 'content2',
                        allowBlank : false
                    }, {
                        name : "url3",
                        allowBlank : false
                    }, {
                        name : 'content3',
                        allowBlank : false
                    }, {
						name : "createDate",
						type : 'String'
					} ]
				})
			});

			store.on('beforeload', function(thiz, options) {
				Ext.apply(thiz.baseParams, {

				});
			});

			return store;
		},
		getForm : function() {
			var frm = new Ext.form.FormPanel({
				fileUpload: true,
				url : appPath + '/advShelf/save.do',
				labelAlign : 'left',
				buttonAlign : 'center',
				bodyStyle : 'padding:5px',
				frame : true,
				labelWidth : 150,
				defaultType : 'textfield',
				defaults : {
					allowBlank : false,
					anchor : '90%',
					enableKeyEvents : true
				},
				items : [
						{
							xtype : 'hidden',
							name : 'id',
							id:'ids',
							value : '0'
						},
						{
							xtype:'hidden',name:'positionType',value:'1'
						},
						{
							name : 'name',
							fieldLabel : '广告名称',
							maxLength : 30
						},
						new Ext.form.ComboBox({
                            hiddenName : 'typeId',
                            fieldLabel : '广告类型',
                            store : new Ext.data.SimpleStore({
                                fields : [ 'text', 'value' ],
                                data : [ [ '纯文字', '1' ], [ '图片', '2' ] ]
                            }),
                            listeners:{'select':function(){
                                if(this.value==1){
                                     Ext.getCmp("adv_file").setVisible(false);
                                     Ext.getCmp("adv_url1").setVisible(true);
                                     Ext.getCmp("adv_url2").setVisible(true);
                                     Ext.getCmp("adv_url3").setVisible(true);
                                     Ext.getCmp("adv_content1").setVisible(true);
                                     Ext.getCmp("adv_content2").setVisible(true);
                                     Ext.getCmp("adv_content3").setVisible(true);
                                }
                                if(this.value==2){
                                 Ext.getCmp("adv_file").setVisible(true);
                                 Ext.getCmp("adv_file").setWidth(500);
                                 Ext.getCmp("adv_url1").setVisible(false);
                                 Ext.getCmp("adv_url2").setVisible(false);
                                 Ext.getCmp("adv_url3").setVisible(false);
                                 Ext.getCmp("adv_content1").setVisible(false);
                                 Ext.getCmp("adv_content2").setVisible(false);
                                 Ext.getCmp("adv_content3").setVisible(false);
                                }

                            }},
                            mode : 'local',
                            displayField : 'text',
                            valueField : 'value',
                            triggerAction : 'all',
                            editable : false
                        }),
						new Ext.form.ComboBox({
							hiddenName : 'platformId',
							fieldLabel : '广告平台',
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
							mode : 'local',
							displayField : 'name',
							valueField : 'id',
							triggerAction : 'all',
							editable : false
						}),
						{
                            name : 'versions',
                            fieldLabel : '版本号（多个以;分隔）',
                            regex: /^[0-9.;]+$/,
                             regexText: '输入数字、逗号、分号',
                            maxLength : 200
                        },
                         new Ext.form.ComboBox({
                            hiddenName : 'isUseDitch',
                            fieldLabel : '是否分渠道',
                            store : new Ext.data.SimpleStore({
                                fields : [ 'text', 'value' ],
                                data : [ [ '是', '1' ], [ '否', '0' ] ]
                            }),
                            mode : 'local',
                            displayField : 'text',
                            valueField : 'value',
                            triggerAction : 'all',
                            editable : false
                        }),
                        {
                            name : 'ditchIds',
                             regex: /^[0-9a-zA-Z;.]+$/,
                             regexText: '输入汉字数字或字母',
                            fieldLabel : '渠道号（以;分隔）',
                            maxLength : 200,
                            height:60,
                            type:'textarea'
                        },
						{
                            xtype: 'fileuploadfield',
                            fieldLabel: '广告图片',
                            name: 'file',
                            id: 'adv_file',

                            hidden:true,
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
                        	name : 'content1',
                        	fieldLabel : '广告内容1',
                        	id:'adv_content1',
                        	maxLength : 100
                        },
						{
							name : 'url1',
							fieldLabel : 'URL1',
							id:'adv_url1',
							vtype: 'url',
		                    vtypeText: '输入正确的url地址',
							maxLength : 200
						},{
                            name : 'content2',
                            id:'adv_content2',
                            fieldLabel : '广告内容2',
                            maxlength : 100
                         },
                        {
                            name : 'url2',
                            fieldLabel : 'URL2',
                            id:'adv_url2',
                            vtype: 'url',
                            vtypeText: '输入正确的url地址',
                            maxLength : 200
                        },
                         {
                            name : 'content3',
                             id:'adv_content3',
                            fieldLabel : '广告内容3',
                            maxlength : 100
                         },
                        {
                            name : 'url3',
                            fieldLabel : 'URL3',
                            id:'adv_url3',
                            vtype: 'url',
                            vtypeText: '输入正确的url地址',
                            maxLength : 200
                        }
                        , new Ext.form.ComboBox({
							hiddenName : 'status',
							fieldLabel : '状态',
							store : new Ext.data.SimpleStore({
								fields : [ 'text', 'value' ],
								data : [ [ '上线', '1' ], [ '下线', '0' ] ]
							}),
							mode : 'local',
							displayField : 'text',
							valueField : 'value',
							triggerAction : 'all',
							editable : false
						}) ],
				buttons : [ {
					text : '保存',
					iconCls : 'icon-save',
					scope : this,
					handler : function() {
						if (this.frm.form.isValid()) {
							this.frm.form.submit({
								success : function(form, action) {
									Ext.Msg.alert('提示:', action.result.info);
									form.reset();
									App.bookshelfAdvList.store.reload();
									App.bookshelfAdvList.dlg.hide();
								},
								failure : function(form, action) {
									Ext.Msg.alert('提示:', action.result.info);
								},
								waitMsg : '正在保存数据，稍后...'
							});
						}
					}
				}, {
					text : '重置',
					iconCls : 'icon-cross',
					scope : this,
					handler : function() {
						this.frm.form.reset();
						this.frm.form.clearInvalid();
					}
				} ]
			// buttons
			}); // FormPanel

			return frm;
		},

		getDialog : function() {
			var dlg = new Ext.Window({
				width : 750,
				height : 480,
				title : '',
				plain : true,
				closable : true,
				resizable : false,
				frame : true,
				layout : 'fit',
				closeAction : 'hide',
				border : false,
				modal : true,
				items : [ this.frm ],
				listeners : {
					scope : this,
					render : function(fp) {
						this.frm.form.waitMsgTarget = fp.getEl();
					},
					show : function() {
						this.frm.form.setValues(this.currentFormValues);
						this.frm.form.clearInvalid();
					}
				}
			}); // dlg
			return dlg;
		},

		createGrid : function(id) {

			var panel = Ext.getCmp(id);

			panel.body.dom.innerHTML = "";

			var sm = new Ext.grid.CheckboxSelectionModel();

			var searchBar = new Ext.Toolbar({
				renderTo : Ext.grid.GridPanel.tbar,
				items : [
						'广告ID：',
						{
							xtype : 'textfield',
							id : 'bookshelfAdvList.id',
							width : 80
						},
						{
							xtype : 'tbseparator'
						},
						'平台：',
						new Ext.form.ComboBox({
							id : 'bookshelfAdvList.platformId',
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
		                            listeners: {'load': function () {
		                                this.add(allRecord);
		                            }
		                            }
		                        }),
							mode : 'local',
							displayField : 'name',
							valueField : 'id',
							triggerAction : 'all',
							editable : false
						}),
						{
							xtype : 'tbseparator'
						},
						'书架ID：',
						{
							xtype : 'textfield',
							id : 'bookshelfAdvList.bookshelfId',
							width : 80
						},
						 
						{
							xtype : 'tbseparator'
						},
						'广告标题：',
						{
							xtype : 'textfield',
							id : 'bookshelfAdvList.title',
							width : 80
						},
						{
							xtype : 'tbseparator'
						},
						'创建日期：',
						{
							xtype: "datefield",
							id : 'bookshelfAdvList.createDate',
                            format : 'Y-m-d',
							width : 80
						},
						{
							xtype : 'tbseparator'
						},
						'状态：',
						new Ext.form.ComboBox({
							id : 'bookshelfAdvList.status',
							store : new Ext.data.SimpleStore({
								fields : [ 'text', 'value' ],
								data : [ [ '全部', '' ], [ '下线', '0' ],
										[ '上线', '1' ] ]
							}),
							mode : 'local',
							displayField : 'text',
							valueField : 'value',
							triggerAction : 'all',
							editable : false,
							width : 80
						}),

						{
							xtype : 'tbseparator'
						}, {
							xtype : 'button',
							text : '查找',
							iconCls : 'icon-search',
							handler : function() {
								App.bookshelfAdvList.store.load();
							}
						} ]
			});

			this.grid = new Ext.grid.GridPanel({
				loadMask : true,
				tbar : [ {
					xtype : 'button',
					text : '新增',
					iconCls : 'icon-add',
					handler : this.add
				}],
				store : this.store,
				sm : sm,
				columns : [ sm, {
					header : "广告ID",
					width : 100,
					sortable : true,
					dataIndex : 'id',
					align : 'center',
					hideable : false
				}, {
					header : "广告标题",
					width : 150,
					sortable : true,
					dataIndex : 'name',
					align : 'center'
				} , {
					header : "平台",
					width : 100,
					dataIndex : 'platformId',
					renderer:App.zeroRenderAll,
					align : 'center'
				}, {
					header : "创建日期",
					width : 150,
					dataIndex : 'createDate',
					align : 'center'
				}, {
					header : "状态",
					width : 80,
					dataIndex : 'status',
					 renderer: App.statusRender,
					align : 'center'
				}, {
					header : "操作",
					width : 100,
					sortable : false,
					dataIndex : 'id',
					align : 'center',
					renderer : optRender
				} ],
				listeners : {
					'render' : function() {
						searchBar.render(this.tbar);
					}
				},
				bbar : new Ext.PagingToolbar({
					store : this.store,
					pageSize : this.store.baseParams.limit,
					plugins : [ new Ext.ux.PageSizePlugin() ],
					displayInfo : true,
					emptyMsg : '没有找到相关数据'
				})
			});

			function optRender(value, p, record) {
				 
				var ca = '<input type="image" src="'
					+ appPath
					+ '/scripts/icons/page_edit.png" title="编辑" onclick="App.bookshelfAdvList.edit('
					+ value + ')"/>';
				var b="";
				//alert("record.data.status = "+record.data.status);
				if(record.data.status=='1'){
					b='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="image" src="'
						+ appPath
						+ '/scripts/icons/page_edit.png" title="下线" onclick="App.bookshelfAdvList.status_dow('
						+ value + ')"/>'+'<input type="image" src="'+ appPath+ '/scripts/icons/page_edit.png" title="发布" onclick="App.bookshelfAdvList.release('+ value + ')"/>';
				}else{
					b='&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="image" src="'
						+ appPath
						+ '/scripts/icons/page_edit.png" title="上线" onclick="App.bookshelfAdvList.status_up('
						+ value + ')"/>';
				}
				return String.format( ca+b);
			}

			panel.add(this.grid);

		},
		release:function(){
		if (App.bookshelfAdvList.grid.getSelectionModel().hasSelection()) {
        				var recs = App.bookshelfAdvList.grid.getSelectionModel()
        						.getSelections();
        				var ids = 0;
        				for (var i = 0; i < recs.length; i++) {
        					var data = recs[i].data;
        					ids=data.id;
        				}
        				Ext.Msg.confirm('下线', '确定下线', function(btn) {
        					if (btn == 'yes') {
        						Ext.Ajax.request({
        							method : 'post',
        							url : appPath + '/advShelf/release.do',
        							params : {
        								id : ids,
        								status:0
        							},
        							success : function(resp, opts) {
        								var result = Ext.util.JSON
        										.decode(resp.responseText);
        								var info = result.info;
        								if (result.success == 'true') {
        									Ext.Msg.alert('信息', info);
        									App.bookshelfAdvList.store.load();
        								} else {
        									Ext.Msg.alert('信息', info);
        								}
        							},
        							failure : function(resp, opts) {
        								var result = Ext.util.JSON
        										.decode(resp.responseText);
        								var info = result.info;
        								Ext.Msg.alert('提示', info);
        							}
        						});
        					}
        				})

        			}
		},
		status_dow:function(){
			if (App.bookshelfAdvList.grid.getSelectionModel().hasSelection()) {
				var recs = App.bookshelfAdvList.grid.getSelectionModel()
						.getSelections();
				var ids = 0;
				for (var i = 0; i < recs.length; i++) {
					var data = recs[i].data;
					ids=data.id;
				}
				Ext.Msg.confirm('下线', '确定下线', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request({
							method : 'post',
							url : appPath + '/advShelf/changStatus.do',
							params : {
								id : ids,
								status:0
							},
							success : function(resp, opts) {
								var result = Ext.util.JSON
										.decode(resp.responseText);
								var info = result.info;
								if (result.success == 'true') {
									Ext.Msg.alert('信息', info);
									App.bookshelfAdvList.store.load();
								} else {
									Ext.Msg.alert('信息', info);
								}
							},
							failure : function(resp, opts) {
								var result = Ext.util.JSON
										.decode(resp.responseText);
								var info = result.info;
								Ext.Msg.alert('提示', info);
							}
						});
					}
				})
				
			}
		},
		status_up:function(){
			if (App.bookshelfAdvList.grid.getSelectionModel().hasSelection()) {
				var recs = App.bookshelfAdvList.grid.getSelectionModel()
						.getSelections();
				var ids =0;
				for (var i = 0; i < recs.length; i++) {
					var data = recs[i].data;
					ids=data.id;
				}
				Ext.Msg.confirm('上线', '确定上线', function(btn) {
					if (btn == 'yes') {
						Ext.Ajax.request({
							method : 'post',
							url : appPath + '/advShelf/changStatus.do',
							params : {
								id : ids.toString(),
								status:1
							},
							success : function(resp, opts) {
								var result = Ext.util.JSON
										.decode(resp.responseText);
								var info = result.info;
								if (result.success == 'true') {
									Ext.Msg.alert('信息', info);
									App.bookshelfAdvList.store.load();
								} else {
									Ext.Msg.alert('信息', info);
								}
							},
							failure : function(resp, opts) {
								var result = Ext.util.JSON
										.decode(resp.responseText);
								var info = result.info;
								Ext.Msg.alert('提示', info);
							}
						});
					}
				})
			}
		},
		add : function() {
			Ext.apply(App.bookshelfAdvList.currentFormValues, {
				id:'0',
				platformId : "",
				name : "",
				url1 :"",
				content1:"",
                url2 :"",
                content2:"",
                url3 :"",
                content3:"",
                isUseDitch:'',
                ditchIds:'',
                versions:'',
                typeId:1,
				status : ''
			});
			App.bookshelfAdvList.dlg.setTitle("增加资源");
			App.bookshelfAdvList.dlg.show();
		},
		edit : function() {
			if (App.bookshelfAdvList.grid.getSelectionModel().hasSelection()) {
				App.bookshelfAdvList.dlg.setTitle("编辑");
				var rec = App.bookshelfAdvList.grid.getSelectionModel()
						.getSelected();
				Ext.apply(App.bookshelfAdvList.currentFormValues, {
					id : rec.data.id,
					platformId : rec.data.platformId,
					name : rec.data.name,
					url1 : rec.data.url1,
					content1:rec.data.content1,
                    url2 : rec.data.url2,
                    content2:rec.data.content2,
                    url3 : rec.data.url3,
                    content3:rec.data.content3,
                    isUseDitch:rec.data.isUseDitch,
                    ditchIds:rec.data.ditchIds,
                    versions:rec.data.versions,
                    typeId:rec.data.typeId,
					status : rec.data.status
				});
				App.bookshelfAdvList.dlg.show();
			} else {
				Ext.Msg.alert('信息', '请选择要编辑的信息。');
			}
		},

		render : function(id) {
			if (!this.store) {
				this.store = this.getStore();
			}
			;
			if (!this.frm) {
				this.frm = this.getForm();
			}
			; // if(!this.frm)

			if (!this.dlg) {
				this.dlg = this.getDialog();
			}
			; // if(!this.dlg)
			this.createGrid(id);

		},

        init: function(params) {},

        destroy: function() {}
	};
}();
