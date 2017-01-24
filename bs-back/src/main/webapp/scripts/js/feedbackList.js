App.feedbackList = function () {
    
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
                    url: appPath + '/feedback/list.do'
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
                            name: 'userId',
                            type: 'int'
                        },{
                            name: 'userName',
                            type: 'string'
                        },{
                        	name: "contact",
                            type: 'string'
                        },{
                            name: 'content',
                            type: 'string'
                        },{
                        	name: 'ditchId',
                        	type: 'int'
                        },{
                        	name: 'ditchName',
                            type: 'string'
                        },{
                            name: 'platformId',
                            type: 'int'
                        },{
                        	name: 'platformName',
                        	type: 'string'
                        },{
                        	name: 'os',
                            type: 'string'
                        },{
                        	name:"createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: 'nickName',
                            type: 'string'
                        }
                    ]
                })
            });
            
            store.on('beforeload', function (thiz, options) {
            	
            	var startCreateDate = App.dateFormat(Ext.getCmp("feedbackList.startCreateDate").getValue());
            	var endCreateDate = App.dateFormat(Ext.getCmp("feedbackList.endCreateDate").getValue());

            	//结束时间变为当天的最后时间
                if(null!=endCreateDate && ''!=endCreateDate && undefined!=endCreateDate){
                    endCreateDate = endCreateDate.replace("00:00:00","23:59:59");
                }

                Ext.apply(
                    thiz.baseParams,
                    {
                    	id: Ext.getCmp("feedbackList.id").getValue(),
                    	userName: Ext.getCmp("feedbackList.userName").getValue(),
                    	startCreateDate: startCreateDate,
                    	endCreateDate: endCreateDate,
                    	content: Ext.getCmp("feedbackList.content").getValue(),
                    	nickName: Ext.getCmp("feedbackList.nickName").getValue(),
                    	platformId: Ext.getCmp("feedbackList.platformId").getValue()
                    }
                );
            });
            return store;
        },
        
        createGrid: function (id) {
        	
            var panel = Ext.getCmp(id);
            
            panel.body.dom.innerHTML = "";
            
            var sm = new Ext.grid.CheckboxSelectionModel();
            
            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                //在后边的listeners里会将搜索工具条searchBar添加到tbar里
                tbar: [
                    '反馈ID：',
                    {
                        xtype: 'textfield',
                        id: 'feedbackList.id',
                        regex: /^[0-9]+$/,
                        regexText: '输入数字',
                        emptyText: '请输入数字',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '用户名：',
                    {
                        xtype: 'textfield',
                        id: 'feedbackList.userName',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '用户昵称：',
                    {
                        xtype: 'textfield',
                        id: 'feedbackList.nickName',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '平台：',
                    new Ext.form.ComboBox({
                        id: "feedbackList.platformId",
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
                                //"全部"选项
                                this.add(allRecord);
                            }
                            }
                        }),
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false,
                        width: 100
                    }),
                    {xtype: 'tbseparator'},
                    '反馈时间开始',
                    {
                        id: 'feedbackList.startCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                    },
                    {xtype: 'tbseparator'},
                    '反馈时间结束',
                    {
                        id: 'feedbackList.endCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d'
                    },
                    {xtype: 'tbseparator'},
                    '内容：',
                    {
                        xtype: 'textfield',
                        id: 'feedbackList.content',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.feedbackList.store.load();
                        }
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '导出',
                        //iconCls: 'icon-export',
                        handler: function () {
                            App.feedbackList.exportToExcel();
                        }
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [//sm,
                {
                    header: "反馈ID",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center',
                    hideable: false
                },{
                    header: "用户名",
                    width: 120,
                    sortable: true,
                    dataIndex: 'userName',
                    align: 'center'
                },{
                     header: "昵称",
                     width: 120,
                     sortable: true,
                     dataIndex: 'nickName',
                     align: 'center'
                 },{
                    header: "联系方式",
                    width: 120,
                    sortable: true,
                    dataIndex: 'contact',
                    align: 'center'
                }, {
                    header: "反馈内容",
                    width: 120,
                    sortable: true,
                    dataIndex: 'content',
                    align: 'center'
                },{
                    header: "渠道",
                    width: 120,
                    sortable: true,
                    dataIndex: 'ditchId',
                    align: 'center'
                }, {
                    header: "平台",
                    width: 150,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                }, {
                    header: "操作系统",
                    width: 150,
                    sortable: true,
                    dataIndex: 'os',
                    align: 'center'
                }, {
                    header: "反馈日期",
                    width: 120,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                }],
                listeners: {
                    'render': function () {
                    	//将搜索工具条添加到tbar后边(前边有可能有按钮工具条)
                        //searchBar.render(this.tbar);
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
            
            panel.add(this.grid);

        },
        
        //导出成Excel
        exportToExcel:function(){

            if(!Ext.getCmp("feedbackList.id").isValid()){
                Ext.Msg.alert('提示:', '反馈ID必须为数字！');
                return;
            }

            var url = appPath + '/feedback/exportToExcel.do?1=1';
        	//查询条件
        	var id = Ext.getCmp("feedbackList.id").getValue();
        	if(id!=null && id!=undefined && id!=''){
        	    url = url + "&id=" + id;
        	}
        	var userName = Ext.getCmp("feedbackList.userName").getValue();
        	if(userName!=null && userName!=undefined && userName!=''){
                url = url + "&userName=" + userName;
            }
        	var startCreateDate = App.dateFormat(Ext.getCmp("feedbackList.startCreateDate").getValue());
        	if(startCreateDate!=null && startCreateDate!=undefined && startCreateDate!=''){
        	    startCreateDate = startCreateDate.substring(0,11);
                url = url + "&startCreateDate=" + startCreateDate;
            }
       	 	var endCreateDate = App.dateFormat(Ext.getCmp("feedbackList.endCreateDate").getValue());
       	 	if(endCreateDate!=null && endCreateDate!=undefined && endCreateDate!=''){
       	 	    endCreateDate = endCreateDate.substring(0,11);
                url = url + "&endCreateDate=" + endCreateDate;
            }
       	 	var nickName = Ext.getCmp("feedbackList.nickName").getValue();
       	 	if(nickName!=null && nickName!=undefined && nickName!=''){
                url = url + "&nickName=" + nickName;
            }
       	 	var content = Ext.getCmp("feedbackList.content").getValue();
       	 	if(content!=null && content!=undefined && content!=''){
                url = url + "&content=" + content;
            }
       	 	var platformId = Ext.getCmp("feedbackList.platformId").getValue();
       	 	if(platformId!=null && platformId!=undefined && platformId!=''){
                url = url + "&platformId=" + platformId;
            }
       	 	window.open(url);
        },
        
        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            }
            
            this.createGrid(id);

        },

        init: function(params) {},

        destroy: function() {}
    };
}();
