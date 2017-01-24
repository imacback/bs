App.payConfigList = function () {
    return {

        currentFormValues: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                //autoLoad: {},
                autoLoad: false,//首次加载页面时不加载列表信息
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/payConfig/list.do'
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
                            name: 'name',
                            allowBlank: false
                        },{
                            name: "parentPaySwitchId",
                            type: 'int'
                        },{
                            name: "childPaySwitchId",
                            type: 'int'
                        },{
                            name: 'amount',
                            type: 'int'
                        },{
                        	name: "dqAmount",
                            type: 'int'
                        },{
                            name: 'giftAmount',
                            type: 'int'
                        },{
                        	name:'status',
                        	type: 'int'
                        },{
                            name:'orderId',
                            type: 'int'
                        },{
                            name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: "editDate",
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: "parentPaySwitchName",
                            type: 'string'
                        },{
                            name: "childPaySwitchName",
                            type: 'string'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                    	parentPaySwitchId: Ext.getCmp("payConfigList.parentPaySwitchId").getValue(),
                    	childPaySwitchId: Ext.getCmp("payConfigList.childPaySwitchId").getValue(),
                    	status: Ext.getCmp("payConfigList.status").getValue()
                    }
                );

            });
            return store;
        },

        //Form表单中一级支付开关的store
        getParentPaySwitchOptionStore: function (){
            var store = new Ext.data.Store({
                baseParams: {
                    parentId: 0,
                    //上线
                    status: 1
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/paySwitch/list.do'
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

        //Form表单中二级支付开关的store
        getChildPaySwitchOptionStore: function (){
            var store = new Ext.data.Store({
                baseParams: {
                    parentId: -1,//默认不加载任何二级支付开关
                    //上线
                    status: 1
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/paySwitch/list.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name']
                }),
                //添加"无"选项
                listeners: {'load': function () {
                        /*
                          该store加载的都是已上线状态,而在支付开关的"上线"操作时会校验包含已上线的
                          二级支付开关的一级支付开关才能进行上线操作所以这里根据加载的个数进行判断,
                          如果个数为0(表明对应的一级支付开关下不包含二级支付开关)则增加一个"无"的选
                          项,否则不增加"无"项.
                        */
                        if(this.getTotalCount()==0){
                            this.add(zeroRecord);
                        }
                    }
                }
            });
            return store;
        },

        //查询区域中一级支付开关的store
        getParentPaySwitchOptionStore_Search: function (){
            var store = new Ext.data.Store({
                baseParams: {
                    parentId: 0
                    //上线
                    //status: 1
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/paySwitch/list.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name']
                }),
                //添加"无"选项
                listeners: {'load': function () {
                        this.add(allRecord);
                    }
                }
            });
            return store;
        },

        //搜索区域中二级支付开关的store
        getChildPaySwitchOptionStore_Search: function (){
            var store = new Ext.data.Store({
                baseParams: {
                    parentId: -1//默认不加载任何二级支付开关
                    //上线
                    //status: 1
                },
                autoLoad: true,
                remoteSort: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/paySwitch/list.do'
                }),
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: ['id', 'name']
                }),
                //添加"无"选项
                listeners: {'load': function () {
                        /*
                          该store加载的都是已上线状态,而在支付开关的"上线"操作时会校验包含已上线的
                          二级支付开关的一级支付开关才能进行上线操作所以这里根据加载的个数进行判断,
                          如果个数为0(表明对应的一级支付开关下不包含二级支付开关)则增加一个"无"的选
                          项,否则不增加"无"项.
                         */
                        if(this.getTotalCount()==0){
                            this.add(zeroRecord);
                        }

                        this.add(allRecord);
                    }
                }
            });
            return store;
        },

        //编辑支付配置信息的Form
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
            	url: appPath + '/payConfig/save.do',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 100,
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
                    new Ext.form.ComboBox({
                        name: 'parentPaySwitchId',
                        hiddenName: 'parentPaySwitchId',
                        fieldLabel: '一级支付方式',
                        store: App.payConfigList.parentPaySwitchOptionStore,
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        allowBlank: false,
                        editable: false,
                        listeners: {
                            'select': App.payConfigList.changeParentPaySwitch
                        }
                    }),
                    new Ext.form.ComboBox({
                        name: 'childPaySwitchId',
                        hiddenName: 'childPaySwitchId',
                        fieldLabel: '二级支付方式',
                        store: App.payConfigList.childPaySwitchOptionStore,
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        allowBlank: false,
                        editable: false
                    }),
                    {
                        name: 'amount',
                        fieldLabel: '支付金额(元)',
                        allowBlank:false,
                        maxlength: 10,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数'
                    },{
                        name: 'dqAmount',
                        fieldLabel: '兑换金额(多宝)',
                        allowBlank:false,
                        maxlength: 10,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数'
                    },{
                        name: 'giftAmount',
                        fieldLabel: '赠送金额(多宝)',
                        allowBlank:true,
                        maxlength: 10,
                        regex:/^[0-9]\d*$/,
                        regexText:'请输入大于0整数'
                    },{
                        name: 'orderId',
                        fieldLabel: '序号',
                        allowBlank: false,
                        maxLength: 5,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数'
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'status',
                        fieldLabel: '状态',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['上线', '1'],
                                ['下线', '0']
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
                        handler: function (){
                            if (this.frm.form.isValid()){
                                this.frm.form.submit({
                                    //防止空白框提示信息提交到后台
                                    submitEmptyText: false,
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.payConfigList.store.reload();

                                        //支付配置的删除操作有可能会影响一、二级支付开关的发布状态,所以操作成功后同时刷新对应的列表
                                        App.payConfigList.reloadPaySwitch();

                                        App.payConfigList.dlg.hide();
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
                            //重置后不显示校验不通过的红线提示
                            this.frm.form.clearInvalid();
                        }
                    }
                ] //buttons
            }); //FormPanel

            return frm;
        },

        //支付开关
        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 270,
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

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    '一级支付方式：',
                    new Ext.form.ComboBox({
                        id: 'payConfigList.parentPaySwitchId',
                        store: App.payConfigList.parentPaySwitchOptionStore_Search,
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false,
                        width: 100,
                        listeners: {
                            'select': App.payConfigList.changeParentPaySwitch_Search
                        }
                    }),
                    {xtype: 'tbseparator'},
                    '二级支付方式：',
                    new Ext.form.ComboBox({
                        id: 'payConfigList.childPaySwitchId',
                        fieldLabel: '二级支付方式',
                        store: App.payConfigList.childPaySwitchOptionStore_Search,
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        editable: false,
                        width: 100
                    }),
                    {xtype: 'tbseparator'},
                    '状态：',
                    new Ext.form.ComboBox({
                        id: 'payConfigList.status',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['全部', ''],
                                ['下线', '0'],
                                ['上线', '1']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        width: 80
                    }),
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.payConfigList.store.load();
                        }
                    }
                ]
            });
            
            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                //在后边的listeners里会将搜索工具条searchBar添加到tbar里
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增金额',
                        iconCls: 'icon-add',
                        handler: this.add
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [
                //sm,
                {
                    header: "ID",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center',
                    hideable: false
                },{
                    header: "一级支付方式",
                    width: 120,
                    sortable: true,
                    dataIndex: 'parentPaySwitchName',
                    align: 'center'
                },{
                    header: "二级支付方式",
                    width: 120,
                    sortable: true,
                    dataIndex: 'childPaySwitchName',
                    align: 'center'
                },{
                    header: "支付金额(元)",
                    width: 150,
                    sortable: true,
                    dataIndex: 'amount',
                    align: 'center'
                },{
                    header: "兑换金额(多宝)",
                    width: 150,
                    sortable: true,
                    dataIndex: 'dqAmount',
                    align: 'center'
                },{
                    header: "赠送金额(多宝)",
                    width: 150,
                    sortable: true,
                    dataIndex: 'giftAmount',
                    align: 'center'
                },{
                    header: "序号",
                    width: 60,
                    sortable: true,
                    dataIndex: 'orderId',
                    align: 'center'
                },{
                    header: "状态",
                    width: 60,
                    sortable: false,
                    dataIndex: 'status',
                    align: 'center',
                    renderer: statusRender
                },{
                    header: "创建日期",
                    width: 100,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                },{
                    header: "编辑时间",
                    width: 150,
                    sortable: true,
                    dataIndex: 'editDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },{
                    header: "操作",
                    width: 100,
                    sortable: false,
                    dataIndex: 'id',
                    align: 'center',
                    renderer: optRender
                }],
                listeners: {
                    'render': function () {
                    	//将搜索工具条添加到tbar后边(前边有可能有按钮工具条)
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

            //渲染状态
            function statusRender(value, p, record){
                //1表示上线,0表示下线
                var status = record.data['status'];
                if(status=='1'){
                    return '上线';
                }else{
                    return '下线';
                }
            }

            //渲染操作
            function optRender(value, p, record) {
            	var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.payConfigList.edit(' + value + ')"/>';
            	//1表示上线,0表示下线
                var status = record.data['status'];
                var statusStr = '';
                if(status=='1'){
                    statusStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_down.png" title="下线" onclick="App.payConfigList.optStatus(' + value + ')"/>';
                }else{
                    statusStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_up.png" title="上线" onclick="App.payConfigList.optStatus(' + value + ')"/>';
                }
            	var deleteStr = '<input type="image" src="' + appPath + '/scripts/icons/delete.png" title="删除" onclick="App.payConfigList.deletePayConfig(' + value + ')"/>';
                return String.format(editStr + '&nbsp;&nbsp;' + statusStr + '&nbsp;&nbsp;' + deleteStr);
            }
            
            panel.add(this.grid);

        },

        //支付开关信息编辑
        edit: function() {
            if (App.payConfigList.grid.getSelectionModel().hasSelection()) {
                App.payConfigList.dlg.setTitle("编辑支付配置");
                var rec = App.payConfigList.grid.getSelectionModel().getSelected();

                //根据回显值重新加载二级下拉框的store并设置二级的回显值
                App.payConfigList.setParentPaySwitch(rec.data.parentPaySwitchId, rec.data.childPaySwitchId);

                Ext.apply(App.payConfigList.currentFormValues, {
                    id: rec.data.id,
                    parentPaySwitchId: rec.data.parentPaySwitchId,
                    childPaySwitchId: rec.data.childPaySwitchId,
                    amount: rec.data.amount,
                    dqAmount: rec.data.dqAmount,
                    giftAmount: rec.data.giftAmount,
                    orderId: rec.data.orderId,
                    status: rec.data.status
                });

                App.payConfigList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的支付配置。');
            }
        },

        //增加支付配置
        add: function () {
            Ext.apply(App.payConfigList.currentFormValues, {
                id: "",
                parentPaySwitchId: "",
                childPaySwitchId: "",
                amount: "",
                dqAmount: "",
                giftAmount: "",
                orderId: "",
                status: ""
            });
            App.payConfigList.dlg.setTitle("增加支付配置");
            App.payConfigList.dlg.show();
        },

        //上线/下线
        optStatus:function(){
            if (App.payConfigList.grid.getSelectionModel().hasSelection()) {
                var rec = App.payConfigList.grid.getSelectionModel().getSelected();
                var id = rec.data.id;
                var status = rec.data.status;

                var msg = '确定要对['+id+']进行上线操作?';
                var statusValue = 1;
                if(status){
                    msg = '确定要对['+id+']进行下线操作?'
                    statusValue = 0;
                }

                Ext.Msg.confirm('信息提示',msg,function(btn){
                    if(btn=='yes'){
                        Ext.Ajax.request({
                            url: appPath + '/payConfig/optStatus.do',
                            params: {id:id,statusValue:statusValue},
                            method: 'POST',
                            success: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
                                } else {
                                    Ext.Msg.alert('信息', info);
                                }
                                //重新加载列表信息
                                App.payConfigList.store.reload();

                                //支付配置的上下线操作有可能会影响一、二级支付开关的发布状态,所以操作成功后同时刷新对应的列表
                                App.payConfigList.reloadPaySwitch();
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
                        });
                    }
                },this);
            }else{
                Ext.Msg.alert('信息', '请选择要操作的支付配置。');
            }
        },

        //删除
        deletePayConfig:function(){
            if (App.payConfigList.grid.getSelectionModel().hasSelection()) {
                var rec = App.payConfigList.grid.getSelectionModel().getSelected();
                var id = rec.data.id;
                Ext.Msg.confirm('信息提示','确定要将删除支付配置['+id+']?',function(btn){
                    if(btn=='yes'){
                        Ext.Ajax.request({
                            url: appPath + '/payConfig/del.do',
                            params: {id:id},
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
                                App.payConfigList.store.reload();

                                //支付配置的删除操作有可能会影响一、二级支付开关的发布状态,所以操作成功后同时刷新对应的列表
                                App.payConfigList.reloadPaySwitch();
                            },
                            failure: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                Ext.Msg.alert('提示', info);
                            }
                        });
                    }
                },this);
            } else {
                Ext.Msg.alert('信息', '请选择要删除的评论。');
            }
        },

        //Form表单中一级支付开发变更事件
        changeParentPaySwitch: function () {
            App.payConfigList.frm.getForm().findField('childPaySwitchId').enable();
            App.payConfigList.frm.getForm().findField('childPaySwitchId').reset();
            var parentId = App.payConfigList.frm.form.findField("parentPaySwitchId").getValue();

            App.payConfigList.childPaySwitchOptionStore.proxy = new Ext.data.HttpProxy({url: appPath + '/paySwitch/list.do?parentId=' + parentId});
            App.payConfigList.childPaySwitchOptionStore.load();
        },

        setParentPaySwitch: function (parentId, childId) {
            App.payConfigList.frm.getForm().findField('childPaySwitchId').enable();
            App.payConfigList.frm.getForm().findField('childPaySwitchId').reset();
            App.payConfigList.childPaySwitchOptionStore.proxy = new Ext.data.HttpProxy({url: appPath + '/paySwitch/list.do?parentId=' + parentId});

            //加载后设置回显值
            App.payConfigList.childPaySwitchOptionStore.load({
                callback:function(records){
                    App.payConfigList.frm.getForm().findField('childPaySwitchId').setValue(childId);
                }
            });
        },

        //查询区域中一级支付开发变更事件
        changeParentPaySwitch_Search: function () {
            Ext.getCmp("payConfigList.childPaySwitchId").enable();
            Ext.getCmp("payConfigList.childPaySwitchId").reset();
            var parentId = Ext.getCmp("payConfigList.parentPaySwitchId").getValue();

            //一级支付方式选择"全部"
            if(parentId==''){
                App.payConfigList.childPaySwitchOptionStore_Search.proxy = new Ext.data.HttpProxy({url: appPath + '/paySwitch/list.do?parentId=-1'});
                App.payConfigList.childPaySwitchOptionStore_Search.load();
                return;
            }

            App.payConfigList.childPaySwitchOptionStore_Search.proxy = new Ext.data.HttpProxy({url: appPath + '/paySwitch/list.do?parentId=' + parentId});
            App.payConfigList.childPaySwitchOptionStore_Search.load();
        },

        //刷新一、二级支付开关列表
        reloadPaySwitch:function(){
            if(App.mainPanel.getItem('paySwitchList')){
                App.paySwitchList.store.reload();
            }
            if(App.mainPanel.getItem('childPaySwitchList')){
                App.childPaySwitchList.store.reload();
            }
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };

            //Form表单中一级支付store
            if(!this.parentPaySwitchOptionStore) {
                this.parentPaySwitchOptionStore = this.getParentPaySwitchOptionStore();
            };
            //Form表单中二级支付store
            if (!this.childPaySwitchOptionStore) {
                this.childPaySwitchOptionStore = this.getChildPaySwitchOptionStore();
            };
            //搜索区域中一级支付store
            if(!this.parentPaySwitchOptionStore_Search) {
                this.parentPaySwitchOptionStore_Search = this.getParentPaySwitchOptionStore_Search();
            };
            //搜索区域中二级支付store
            if (!this.childPaySwitchOptionStore_Search) {
                this.childPaySwitchOptionStore_Search = this.getChildPaySwitchOptionStore_Search();
            };

            if (!this.frm) {
                this.frm = this.getForm();
            };
            if (!this.dlg) {
                this.dlg = this.getDialog();
            };

            this.createGrid(id);

        },

        init: function(params) {
        },

        destroy: function(){
            this.store.destroy();
            this.frm.destroy();
            this.dlg.destroy();

            this.parentPaySwitchOptionStore.destroy();
            this.childPaySwitchOptionStore.destroy();
            this.parentPaySwitchOptionStore_Search.destroy();
            this.childPaySwitchOptionStore_Search.destroy();
        }
    };
}();
