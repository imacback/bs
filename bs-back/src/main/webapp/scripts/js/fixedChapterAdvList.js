App.fixedChapterAdvList = function() {
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
					url : appPath + '/adv/list.do'
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
						name : 'bookId',
						allowBlank : false
					}, {
						name : 'chapterId',
						type : 'int'
					}, {
						name : 'platformId',
						type : 'int' 
					}, {
						name : 'title',
						allowBlank : false
					}, {
						name : 'status',
						type : 'int' 
					}, {
						name : "url",
						allowBlank : false
					}, {
						name : 'content',
						allowBlank : false
					}, {
						name : "createDate",
						type : 'String'
					} ]
				})
			});

			store.on('beforeload', function(thiz, options) {
				Ext.apply(thiz.baseParams, {
					id : Ext.getCmp("fixedChapterAdvList.id").getValue(),
					bookId : Ext.getCmp("fixedChapterAdvList.bookId").getValue(),
					platformId : Ext.getCmp("fixedChapterAdvList.platformId").getValue(),
					title : Ext.getCmp("fixedChapterAdvList.title").getValue(),
					positionType:2,
					createDate:Ext.getCmp("fixedChapterAdvList.createDate").getValue(),
					chapterId : Ext.getCmp("fixedChapterAdvList.chapterId").getValue(),
					status : Ext.getCmp("fixedChapterAdvList.status").getValue()
				});
			});

			return store;
		},
		getForm : function() {
			var frm = new Ext.form.FormPanel({
				fileUpload: true,
				url : appPath + '/adv/save.do',
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
							xtype:'hidden',name:'positionType',value:'2'
						},
						{
							name : 'title',
							fieldLabel : '广告标题',
							maxlength : 30 
						},
						{
							name : 'bookId',
							fieldLabel : '书籍ID',
							maxlength : 100,
							regex: /^[0-9]+$/,
							regexText: '只能输入数字',
							listeners : {
								scope : this,
								keypress : function(field, e) {
									if (e.getKey() == 13) {
										var obj = this.frm.form
												.findField("bookId");
										if (obj)
											obj.focus();
									}
								} 
							}
						},
						{
							name : 'chapterId',
							fieldLabel : '章节ID',
							maxlength : 100,
							regex: /^[0-9]+$/,
							regexText: '只能输入数字',
							listeners : {
								scope : this,
								keypress : function(field, e) {
									if (e.getKey() == 13) {
										var obj = this.frm.form
												.findField("chapterId");
										if (obj)
											obj.focus();
									}
								} 
							}
						},
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
		                            }
		                        }),
							mode : 'local',
							displayField : 'name',
							valueField : 'id',
							triggerAction : 'all',
							editable : false
						}),
						{
							name : 'content',
							fieldLabel : '广告内容',
							height:60,
							type:'textarea',
							maxlength : 100 
						},
						{
                            xtype: 'fileuploadfield',
                            fieldLabel: '封面',
                            name: 'file',
                            id: 'adv.pic',
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
							name : 'url',
							fieldLabel : 'URL',
							 vtype: 'url',
		                        vtypeText: '输入正确的url地址',
							maxlength : 200 
						}, new Ext.form.ComboBox({
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
									App.fixedChapterAdvList.store.reload();
									App.fixedChapterAdvList.dlg.hide();
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
				width : 650,
				height : 350,
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
							id : 'fixedChapterAdvList.id',
							width : 80
						},
						{
							xtype : 'tbseparator'
						},
						'平台：',
						new Ext.form.ComboBox({
							id : 'fixedChapterAdvList.platformId',
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
						'书籍ID：',
						{
							xtype : 'textfield',
							id : 'fixedChapterAdvList.bookId',
							width : 80
						},
						{
							xtype : 'tbseparator'
						},
						'章节ID：',
						{
							xtype : 'textfield',
							id : 'fixedChapterAdvList.chapterId',
							width : 80
						},
						
						{
							xtype : 'tbseparator'
						},
						'广告标题：',
						{
							xtype : 'textfield',
							id : 'fixedChapterAdvList.title',
							width : 80
						},
						{
							xtype : 'tbseparator'
						},
						'创建日期：',
						{
							xtype: "datefield",
							id : 'fixedChapterAdvList.createDate',
                            format : 'Y-m-d',
							width : 80
						},
						{
							xtype : 'tbseparator'
						},
						'状态：',
						new Ext.form.ComboBox({
							id : 'fixedChapterAdvList.status',
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
								App.fixedChapterAdvList.store.load();
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
					dataIndex : 'title',
					align : 'center'
				}, {
					header : "广告内容",
					width : 300,
					sortable : true,
					dataIndex : 'content',
					align : 'center'
				}, {
					header : "书籍ID",
					width : 60,
					sortable : true,
					dataIndex : 'bookId',
					align : 'center'
				}, {
					header : "书籍名称",
					width : 100,
					sortable : true,
					dataIndex : 'bookId',
					align : 'center'
				}, {
					header : "章节",
					width : 60,
					sortable : true,
					dataIndex : 'chapterId',

					align : 'center'
				}, {
					header : "章节名",
					width : 100,
					dataIndex : 'chapterId',
					align : 'center'
				}, {
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
					+ '/scripts/icons/page_edit.png" title="编辑" onclick="App.fixedChapterAdvList.edit('
					+ value + ')"/>';
				var b="";
				//alert("record.data.status = "+record.data.status);
				if(record.data.status=='1'){
					b='<input type="image" src="'
                                        						+ appPath
                                        						+ '/scripts/icons/page_edit.png" title="下线" onclick="App.fixedChapterAdvList.status_dow('
                                        						+ value + ')"/>'
                      +'<input type="image" src="'+ appPath+ '/scripts/icons/page_edit.png" title="发布" onclick="App.fixedChapterAdvList.release('+ value + ')"/>';

				}else{
					b='<input type="image" src="'
						+ appPath
						+ '/scripts/icons/page_edit.png" title="上线" onclick="App.fixedChapterAdvList.status_up('
						+ value + ')"/>';
				}
				return String.format( ca+b);
			}

			panel.add(this.grid);

		},
		release:function(){
		if (App.fixedChapterAdvList.grid.getSelectionModel().hasSelection()) {
        				var recs = App.fixedChapterAdvList.grid.getSelectionModel()
        						.getSelections();
        				var ids = 0;
        				for (var i = 0; i < recs.length; i++) {
        					var data = recs[i].data;
        					ids=data.id;
        				}
        				Ext.Msg.confirm('发布', '确定发布', function(btn) {
        					if (btn == 'yes') {
        						Ext.Ajax.request({
        							method : 'post',
        							url : appPath + '/adv/release.do',
        							params : {
        								id : ids,
        								positionType:2
        							},
        							success : function(resp, opts) {
        								var result = Ext.util.JSON
        										.decode(resp.responseText);
        								var info = result.info;
        								if (result.success == 'true') {
        									Ext.Msg.alert('信息', info);
        									App.fixedChapterAdvList.store.load();
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
			if (App.fixedChapterAdvList.grid.getSelectionModel().hasSelection()) {
				var recs = App.fixedChapterAdvList.grid.getSelectionModel()
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
							url : appPath + '/adv/changStatus.do',
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
									App.fixedChapterAdvList.store.load();
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
			if (App.fixedChapterAdvList.grid.getSelectionModel().hasSelection()) {
				var recs = App.fixedChapterAdvList.grid.getSelectionModel()
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
							url : appPath + '/adv/changStatus.do',
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
									App.fixedChapterAdvList.store.load();
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
			Ext.apply(App.fixedChapterAdvList.currentFormValues, {
				id:'0',
				bookId : "",
				chapterId :"",
				platformId : "",
				title : "",
				url :"",
				content:"",
				status : ''
			});
			App.fixedChapterAdvList.dlg.setTitle("增加资源");
			App.fixedChapterAdvList.dlg.show();
		},
		edit : function() {
			if (App.fixedChapterAdvList.grid.getSelectionModel().hasSelection()) {
				App.fixedChapterAdvList.dlg.setTitle("编辑");
				var rec = App.fixedChapterAdvList.grid.getSelectionModel()
						.getSelected();
				Ext.apply(App.fixedChapterAdvList.currentFormValues, {
					id : rec.data.id,
					bookId : rec.data.bookId,
					chapterId : rec.data.chapterId,
					platformId : rec.data.platformId,
					title : rec.data.title,
					url : rec.data.url,
					content:rec.data.content,
					status : rec.data.status
				});
				App.fixedChapterAdvList.dlg.show();
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
