App.childPaySwitchList = function () {
    return {

        currentFormValues: {},

        //用于缓存从一级支付开关带过来的父ID、平台ID、版本信息、渠道信息、是否分渠道
        parentId:-1,
        parentPlatformId:-1,
        parentVersionValues:'',
        parentDitchValues:'',
        parentIsDitch:1,

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/paySwitch/list.do'
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
                            name: "platformId",
                            type: 'int'
                        },{
                            name: 'versionValues',
                            type: 'string'
                        },{
                        	name: "isDitch",
                            type: 'int'
                        },{
                            name: 'ditchValues',
                            type: 'string'
                        },{
                        	name:'status',
                        	type: 'int'
                        },{
                            name:'isUse',
                            type: 'int'
                        },{
                            name:'orderId',
                            type: 'int'
                        },{
                            name:'parentId',
                            type: 'int'
                        },{
                            name:'isLeaf',
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
                            name:'platformName',
                            type:'string'
                        },{
                            name:'parentName',
                            type:'string'
                        },{
                            name: "payTypeId",
                            type: 'int'
                        },{
                            name:'payTypeName',
                            type:'string'
                        },{
                            name:'tips',
                            type:'string'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                    	//parentId是为了在一级支付页面跳转的超链接使用的
                        parentId: App.childPaySwitchList.parentId
                    }
                );
            });
            return store;
        },

        //编辑支付开关信息的Form
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                method: 'POST',
                layout: 'form',
            	url: appPath + '/paySwitch/save.do',
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
                    },{//父ID,改ID是从点击一级支付开关跳转时带过来的
                        xtype: 'hidden',
                        name: 'parentId',
                        value: App.childPaySwitchList.parentId
                    },{//平台ID,和其父节点相同
                        xtype: 'hidden',
                        name: 'platformId',
                        value: App.childPaySwitchList.parentPlatformId
                    },{//新建时默认为未发布
                        xtype: 'hidden',
                        name: 'isUse',
                        value: 0
                    },{//二级支付isLeaf为1
                        xtype: 'hidden',
                        name: 'isLeaf',
                        value: 1
                    },{
                        name: 'name',
                        fieldLabel: '二级支付名称',
                        allowBlank:false,
                        maxlength: 25
                        //regex: /^[0-9a-zA-Z]+$/,
                        //regexText: '只能输入字母数字',
                    },
                    {
                        name: 'versionValues',
                        fieldLabel: '版本号',
                        allowBlank:false,
                        //value: App.childPaySwitchList.parentVersionValues,
                        emptyText: '多版本请用;隔开'
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'isDitch',
                        fieldLabel: '是否分渠道',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['否', '0'],
                                ['是', '1']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        listeners:{
                            'select': function(combo,records,index){
                                //是否分渠道选中"是"时,则"渠道号"项为非必填项
                                var dataObj = Ext.getCmp("childPaySwitchList.ditchValues");
                                if(combo.getValue()=='0'){
                                    //不区分渠道时,渠道号为空且为disabled
                                    dataObj.allowBlank = true;
                                    dataObj.setValue("");
                                    //dataObj.setReadOnly(true);
                                    dataObj.setDisabled(true);
                                }else{//需要区分渠道时,渠道号不能为空且为enabled
                                    dataObj.allowBlank = false;
                                    //dataObj.setReadOnly(false);
                                    dataObj.setDisabled(false);
                                }
                            }
                        }
                    }),
                    {
                        name: 'ditchValues',
                        id:'childPaySwitchList.ditchValues',
                        fieldLabel: '渠道号',
                        allowBlank:true,
                        //value: App.childPaySwitchList.parentDitchValues,
                        emptyText: '多渠道请用;隔开,不填则表示全部渠道'
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'payTypeId',
                        fieldLabel: '支付类型',
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: {isUse: 1},
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/payType/getAllPayType.do'
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
                        allowBlank: false,
                        editable: false
                    }),{
                        name: 'orderId',
                        fieldLabel: '序号',
                        allowBlank: false,
                        maxLength: 5,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数',
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("status");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
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
                    }),{
                        xtype: 'htmleditor',
                        name: 'tips',
                        fieldLabel: '温馨提示',
                        height: 130,
                        maxLength: 50,
                        //关闭"左对齐、中间对齐、右对齐"按钮
                        enableAlignments: false,
                        //字体颜色、高亮按钮
                        enableColors:true,
                        //字体按钮
                        enableFont: false,
                        //增加、减小字号按钮
                        enableFontSize: true,
                        //黑体、斜体、下划线按钮
                        enableFormat: true,
                        //超链接按钮
                        enableLinks: false,
                        //列表按钮
                        enableLists: false,
                        //切换到源编辑按钮
                        enableSourceEdit: true
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

                                //重名校验
                                var parentId = this.frm.form.findField("parentId").getValue();
                                var id = this.frm.form.findField("id").getValue();
                                var name = this.frm.form.findField("name").getValue();
                                if(App.childPaySwitchList.isExistSameName(parentId, id, name)){
                                    Ext.Msg.alert('提示:', "支付方式["+name+"]在数据库中已存在！");
                                    return false;
                                }

                                //校验版本号是否为一级支付版本信息的子集
                                var nonSubsetVersion = App.childPaySwitchList.isVersionSubset();
                                if(nonSubsetVersion.length>0){
                                    Ext.Msg.alert('提示:', "以下版本号与一级支付冲突:<br/>"+nonSubsetVersion);
                                    return false;
                                }

                                //校验渠道号是否为一级支付渠道号的子集
                                var nonSubsetDitch = App.childPaySwitchList.isDitchSubset();
                                if(nonSubsetDitch.length>0){
                                    Ext.Msg.alert('提示:', "以下渠道号与一级支付冲突:<br/>"+nonSubsetDitch);
                                    return false;
                                }

                                this.frm.form.submit({
                                    //防止空白框提示信息提交到后台
                                    submitEmptyText: false,
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.childPaySwitchList.store.reload();

                                        //二级支付的编辑或新增操作有可能会影响一级支付开关的发布状态,所以操作成功后同时刷新一级支付开关列表
                                        App.childPaySwitchList.reloadParentPaySwitch();

                                        //二级支付的编辑或新增可能会影响支付配置页面搜索区域和新增/修改Form中的二级级下拉框的值,
                                        //所以如果支付配置打开,则重新加载对应的两个store
                                        App.childPaySwitchList.reloadPayConfigOptionStore();

                                        App.childPaySwitchList.dlg.hide();
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
                            //this.frm.form.reset();

                            this.frm.form.findField("name").setValue('');
                            this.frm.form.findField("versionValues").setValue(App.childPaySwitchList.parentVersionValues);
                            this.frm.form.findField("isDitch").setValue(App.childPaySwitchList.parentIsDitch);
                            this.frm.form.findField("ditchValues").setValue(App.childPaySwitchList.parentDitchValues);

                            //如果一级支付不区分渠道号,则"渠道号"这一项得变为disable且允许为空
                            if(App.childPaySwitchList.parentIsDitch=='0'){
                                this.frm.form.findField("ditchValues").allowBlank = true;
                                this.frm.form.findField("ditchValues").setDisabled(true);
                            }else{
                                this.frm.form.findField("ditchValues").allowBlank = false;
                                this.frm.form.findField("ditchValues").setDisabled(false);
                            }

                            this.frm.form.findField("payTypeId").setValue('');
                            this.frm.form.findField("orderId").setValue('');
                            this.frm.form.findField("status").setValue('');
                            this.frm.form.findField("tips").setValue('');

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
                width: 470,
                height: 370,
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

            /*
            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    '平台：',
                    new Ext.form.ComboBox({
                        id: "childPaySwitchList.platformId",
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
                                this.(addallRecord);
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
                    '状态：',
                    new Ext.form.ComboBox({
                        id: 'childPaySwitchList.status',
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
                    //parentId是为了在一级支付页面跳转的超链接使用的
                    {
                        xtype: 'hidden',
                        id: 'childPaySwitchList.parentId',
                        value: ''
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.childPaySwitchList.store.load();
                        }
                    }
                ]
            });
            */
            
            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                //在后边的listeners里会将搜索工具条searchBar添加到tbar里
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
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
                    width: 40,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center',
                    hideable: false
                },{
                    header: "一级支付方式",
                    width: 140,
                    sortable: true,
                    dataIndex: 'parentName',
                    align: 'center'
                },{
                    header: "二级支付方式",
                    width: 140,
                    sortable: true,
                    dataIndex: 'name',
                    align: 'center'
                },{
                    header: "支付类型",
                    width: 110,
                    sortable: true,
                    dataIndex: 'payTypeName',
                    align: 'center'
                },{
                    header: "平台",
                    width: 100,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                },{
                    header: "版本号",
                    width: 150,
                    sortable: true,
                    dataIndex: 'versionValues',
                    align: 'center'
                },{
                    header: "是否分渠道",
                    width: 80,
                    sortable: false,
                    dataIndex: 'isDitch',
                    align: 'center',
                    renderer: App.statusRender
                },{
                    header: "渠道号",
                    width: 150,
                    sortable: true,
                    dataIndex: 'ditchValues',
                    align: 'center'
                },{
                    header: "序号",
                    width: 50,
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
                    header: "是否发布",
                    width: 70,
                    sortable: false,
                    dataIndex: 'isUse',
                    align: 'center',
                    renderer: App.statusRender
                },{
                    header: "创建日期",
                    width: 100,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                },{
                    header: "编辑时间",
                    width: 130,
                    sortable: true,
                    dataIndex: 'editDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },{
                    header: "操作",
                    width: 80,
                    sortable: false,
                    dataIndex: 'id',
                    align: 'center',
                    renderer: optRender
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
            	var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.childPaySwitchList.edit(' + value + ')"/>';
            	//1表示上线,0表示下线
                var status = record.data['status'];
                var statusStr = '';
                if(status=='1'){
                    statusStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_down.png" title="下线" onclick="App.childPaySwitchList.optStatus(' + value + ')"/>';
                }else{
                    statusStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_up.png" title="上线" onclick="App.childPaySwitchList.optStatus(' + value + ')"/>';
                }
                return String.format(editStr + '&nbsp;&nbsp;' + statusStr);
            }
            
            panel.add(this.grid);

        },

        //二级支付开关信息编辑
        edit: function() {
            if (App.childPaySwitchList.grid.getSelectionModel().hasSelection()) {
                App.childPaySwitchList.dlg.setTitle("编辑二级支付开关");
                var rec = App.childPaySwitchList.grid.getSelectionModel().getSelected();
                Ext.apply(App.childPaySwitchList.currentFormValues, {
                    id: rec.data.id,
                    parentId: rec.data.parentId,
                    isUse: rec.data.isUse,
                    name: rec.data.name,
                    platformId: rec.data.platformId,
                    versionValues: rec.data.versionValues,
                    isDitch: rec.data.isDitch,
                    ditchValues: rec.data.ditchValues,
                    orderId: rec.data.orderId,
                    status: rec.data.status,
                    isLeaf: rec.data.isLeaf,
                    payTypeId: rec.data.payTypeId,
                    tips: Ext.util.Format.htmlDecode(rec.data.tips)
                });
                //如果原来不区分渠道,则"渠道号"项为disabled,否则为enabled
                if(rec.data.isDitch==0){
                    App.childPaySwitchList.frm.form.findField("ditchValues").disable();
                }else{
                    App.childPaySwitchList.frm.form.findField("ditchValues").enable();
                }


                App.childPaySwitchList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的二级支付开关。');
            }
        },

        //增加二级支付配置
        add: function () {
            Ext.apply(App.childPaySwitchList.currentFormValues, {
                id: "",
                parentId: App.childPaySwitchList.parentId,
                isUse: 0,//是否发布
                name: "",
                platformId: App.childPaySwitchList.parentPlatformId,
                versionValues: App.childPaySwitchList.parentVersionValues,
                isDitch: App.childPaySwitchList.parentIsDitch,
                ditchValues: App.childPaySwitchList.parentDitchValues,
                orderId: "",
                status: "",
                isLeaf: 1,
                payTypeId: "",
                tips: ""
            });
            App.childPaySwitchList.dlg.setTitle("增加二级支付开关");
            //如果一级支付分渠道,则二级支付不能变为"不区分渠道",因为"不区分渠道"表示全渠道
            if(App.childPaySwitchList.parentIsDitch==1){
                App.childPaySwitchList.frm.form.findField("isDitch").setReadOnly(true);
                App.childPaySwitchList.frm.form.findField("ditchValues").enable();
            }else{
                //一级支付不分渠道,则二级支付可以设置是否分渠道,并且初始值和一级支付一致
                App.childPaySwitchList.frm.form.findField("isDitch").setReadOnly(false);
                App.childPaySwitchList.frm.form.findField("ditchValues").disable();
            }


            App.childPaySwitchList.dlg.show();
        },

        //上线/下线
        optStatus:function(){
            if (App.childPaySwitchList.grid.getSelectionModel().hasSelection()) {
                var rec = App.childPaySwitchList.grid.getSelectionModel().getSelected();
                var id = rec.data.id;
                var status = rec.data.status;
                var name = rec.data.name;

                var msg = '确定要对['+name+']进行上线操作?';
                var statusValue = 1;
                if(status){
                    msg = '确定要对['+name+']进行下线操作?'
                    statusValue = 0;
                }

                Ext.Msg.confirm('信息提示',msg,function(btn){
                    if(btn=='yes'){
                        /*
                        //二级支付开关则必须有已上线的支付配置,才允许进行上线操作
                        if(statusValue == 1 && !App.paySwitchList.isExistOnLinePayConfig(rec.data.parentId, id)){
                            Ext.Msg.alert('信息', '['+name+']没有已上线的支付配置，不能进行上线操作！');
                            return;
                        }
                        */

                        Ext.Ajax.request({
                            url: appPath + '/paySwitch/optStatus.do',
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
                                App.childPaySwitchList.store.reload();

                                //二级支付的下线的操作有可能会影响一级支付的显示列表,所以下线操作成功后同时刷新一级支付列表
                                App.childPaySwitchList.reloadParentPaySwitch();

                                //二级支付的上下线可能会影响支付配置页面搜索区域和新增/修改Form中的二级级下拉框的值,
                                //所以如果支付配置打开,则重新加载对应的两个store
                                App.childPaySwitchList.reloadPayConfigOptionStore();
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
                Ext.Msg.alert('信息', '请选择要操作的二级支付开关。');
            }
        },

        //校验版本是否为一级支付版本的子集
        isVersionSubset:function(){

            //一级支付的版本信息
            var varsionArr = (App.childPaySwitchList.parentVersionValues).split(";");
            //二级支付的版本信息
            var childVersionArr = (App.childPaySwitchList.frm.form.findField("versionValues").getValue()).split(";");

            var info = "";
            for(var i=0; i<childVersionArr.length; i++){
                var childVersion = childVersionArr[i].trim();
                var isEqu = 0;
                for(var j=0; j<varsionArr.length; j++){
                    if(varsionArr[j].trim() == childVersion){
                        isEqu = 1;
                        break;
                    }
                }

                if(isEqu==0){
                    info = info + "," + childVersion;
                }
            }

            if(info.length>0){
                info = info.substring(1);
            }

            return info;
        },

        //校验渠道号是否为一级支付渠道号的子集
        isDitchSubset:function(){
            //结果信息
            var info = "";
            //一级支付分渠道
            if(App.childPaySwitchList.parentIsDitch==1){
                //一级支付的渠道信息
                var ditchArr = (App.childPaySwitchList.parentDitchValues).split(";");
                //二级支付的渠道信息
                var childDitchArr = (App.childPaySwitchList.frm.form.findField("ditchValues").getValue()).split(";");

                for(var i=0; i<childDitchArr.length; i++){
                    var childDitch = childDitchArr[i].trim();
                    var isEqu = 0;
                    for(var j=0; j<ditchArr.length; j++){
                        if(ditchArr[j].trim() == childDitch){
                            isEqu = 1;
                            break;
                        }
                    }

                    if(isEqu==0){
                        info = info + "," + childDitch;
                    }
                }

                if(info.length>0){
                    info = info.substring(1);
                }
            }
            return info;
        },

        //刷新一级列表
        reloadParentPaySwitch:function(){
            if(App.mainPanel.getItem('paySwitchList')){
                App.paySwitchList.store.reload();
            }
        },

        //刷新支付配置的下拉框
        reloadPayConfigOptionStore:function(){
            if(App.mainPanel.getItem('payConfigList')) {
                App.payConfigList.parentPaySwitchOptionStore.reload();
                App.payConfigList.parentPaySwitchOptionStore_Search.reload();
            }
        },

        /*
        //校验二级支付开关下是否存在已上线的支付配置
        isExistOnLinePayConfig: function(parentId, id) {
            //为数据统一考虑,默认不存在已上线的支付配置
            var result = false;

            if(null!=parentId && parentId!=undefined && parentId!=""
                && null!=id && id!=undefined && id!=""){

                var obj;
                if (window.ActiveXObject) {
                    obj = new ActiveXObject('Microsoft.XMLHTTP');
                }else if(window.XMLHttpRequest) {
                    obj = new XMLHttpRequest();
                }
                var url = appPath + '/payConfig/isExistOnLinePayConfig.do?parentPaySwitchId=' + parentId + '&childPaySwitchId='+id;

                obj.open('GET', url, false);
                obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                obj.send(null);
                var value = Ext.util.JSON.decode(obj.responseText);
                if (value.success == true) {
                    result = true;
                }
            }
            return result;
        },
        */

        //校验二级支付方式是否存在相同的名称
        isExistSameName: function(parentId, id, name) {
            var obj;
            if (window.ActiveXObject) {
                obj = new ActiveXObject('Microsoft.XMLHTTP');
            }else if(window.XMLHttpRequest) {
                obj = new XMLHttpRequest();
            }
            var url = appPath + '/paySwitch/isExistSameName.do?parentId=' + parentId + '&id='+id + '&name=' + name;

            obj.open('GET', url, false);
            obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            obj.send(null);
            var value = Ext.util.JSON.decode(obj.responseText);

            return value.success;
        },

        render: function (id) {
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

        init: function(params) {
        },

        //该方法是在paySwitchList.js中调用,有二级支付的一级支付超链接名称
        init: function (params) {
            //将父ID、平台ID、版本信息、渠道信息、是否分渠道带过来
            App.childPaySwitchList.parentId = params.parentId;
            App.childPaySwitchList.parentPlatformId = params.platformId;
            App.childPaySwitchList.parentVersionValues = params.versionValues;
            App.childPaySwitchList.parentDitchValues = params.ditchValues;
            App.childPaySwitchList.parentIsDitch = params.isDitch;

            this.store.load();
            //App.childPaySwitchList.grid.getBottomToolbar().store.load();
        },

        destroy: function() {
            this.store.destroy();
            this.frm.destroy();
            this.dlg.destroy();
        }
    };
}();
