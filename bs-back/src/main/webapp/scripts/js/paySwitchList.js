App.paySwitchList = function () {
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
                    	platformId: Ext.getCmp("paySwitchList.platformId").getValue(),
                    	status: Ext.getCmp("paySwitchList.status").getValue(),
                    	//只加载一级支付开关
                    	parentId:0
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
                    },{//父ID,一级的父ID为0
                        xtype: 'hidden',
                        name: 'parentId',
                        value: 0
                    },{//新建和变更后默认为未发布
                        xtype: 'hidden',
                        name: 'isUse',
                        value: 0
                    },{
                        name: 'name',
                        fieldLabel: '支付名称',
                        allowBlank:false,
                        maxlength: 25
                        //regex: /^[0-9a-zA-Z]+$/,
                        //regexText: '只能输入字母数字',
                    },
                    new Ext.form.ComboBox({
                        //name: 'platformName',
                        hiddenName: 'platformId',
                        fieldLabel: '平台',
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
                        displayField: 'name',
                        valueField: 'id',
                        triggerAction: 'all',
                        allowBlank: false,
                        editable: false
                    }),
                    {
                        name: 'versionValues',
                        fieldLabel: '版本号',
                        allowBlank:false,
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
                                var dataObj = Ext.getCmp("paySwitchList.ditchValues");
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
                        id:'paySwitchList.ditchValues',
                        fieldLabel: '渠道号',
                        allowBlank:true,
                        emptyText: '多渠道请用;隔开,不填则表示全部渠道'
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'isLeaf',
                        fieldLabel: '是否有二级',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            //注意:是否是叶子节点和是否有二级支付的逻辑值是反着的
                            data: [
                                ['有', '0'],
                                ['无', '1']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        listeners: {
                            'select': function(combo,records,index){
                                //状态
                                var statusObj = App.paySwitchList.frm.form.findField("status");
                                //支付类型
                                var payTypeObj = App.paySwitchList.frm.form.findField("payTypeId");

                                //有二级节点
                                if(combo.getValue()=='0'){
                                    //如果包含二级节点,则状态为未上线状态,只有存在上线的二级节点时才能为上线状态
                                    statusObj.setValue('0');
                                    statusObj.setReadOnly(true);

                                    //如果是父节点,则不需要指定支付类型,允许为空且该项disabled
                                    payTypeObj.allowBlank = true;
                                    payTypeObj.setValue('');
                                    //payTypeObj.setReadOnly(true);
                                    payTypeObj.setDisabled(true);
                                }else{//叶子节点
                                    statusObj.setValue('');
                                    statusObj.setReadOnly(false);

                                    //如果是叶子节点,则需要指定该支付开关的支付类型
                                    payTypeObj.allowBlank = false;
                                    //payTypeObj.setReadOnly(false);
                                    payTypeObj.setDisabled(false);
                                }
                            }
                        }
                    }),
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
                    }),
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
                        name: 'orderId',
                        fieldLabel: '序号',
                        allowBlank: false,
                        maxLength: 5,
                        regex:/^[1-9]\d*$/,
                        regexText:'请输入大于0整数'
                    },{
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
                                if(App.paySwitchList.isExistSameName(parentId, id, name)){
                                    Ext.Msg.alert('提示:', "支付方式["+name+"]在数据库中已存在！");
                                    return false;
                                }

                                //校验编辑操作时版本和渠道信息是否发生变化
                                var isLeaf = this.frm.form.findField("isLeaf").getValue();
                                var versionValues = this.frm.form.findField("versionValues").getValue();
                                var isDitch = this.frm.form.findField("isDitch").getValue();
                                var ditchValues = this.frm.form.findField("ditchValues").getValue();
                                //这里需要将submit的保存操作写到else里，因为Ext.Msg.confirm是异步的,否则在confirm的同时会执行后边的代码
                                if(null!=id && ''!=id){
                                    //包含二级支付的一级支付方式
                                    if(isLeaf==0 && App.paySwitchList.isExistChild(id)){
                                        var info = App.paySwitchList.isChange(id, versionValues, isDitch, ditchValues);
                                        //存在变更了的版本或渠道号信息
                                        if(info.length>0){
                                            Ext.Msg.confirm('信息提示',info+'继续操作会因变化的版本、渠道信息对二级支付方式相关信息产生联动变化，是否继续操作？',function(btn){
                                                if(btn=='yes'){
                                                    App.paySwitchList.commSave();
                                                }
                                            },this);
                                        }else{
                                            App.paySwitchList.commSave();
                                        }
                                    }else{
                                        App.paySwitchList.commSave();
                                    }
                                }else{
                                    App.paySwitchList.commSave();
                                }
                            }
                        }
                    },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function () {
                            //this.frm.form.reset();

                            //考虑到编辑时平台不能修改,所以重置不包括平台项
                            //this.frm.form.findField("platformId").setValue('');
                            this.frm.form.findField("name").setValue('');
                            this.frm.form.findField("versionValues").setValue('');
                            this.frm.form.findField("isDitch").setValue('');
                            this.frm.form.findField("ditchValues").setValue('');
                            this.frm.form.findField("orderId").setValue('');
                            this.frm.form.findField("status").setValue('');
                            this.frm.form.findField("isLeaf").setValue('');
                            this.frm.form.findField("payTypeId").setValue('');
                            this.frm.form.findField("tips").setValue('');

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
                width: 470,
                height: 450,
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
                    '平台：',
                    new Ext.form.ComboBox({
                        id: "paySwitchList.platformId",
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
                    '状态：',
                    new Ext.form.ComboBox({
                        id: 'paySwitchList.status',
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
                            App.paySwitchList.store.load();
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
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add
                    },
                    {xtype : 'tbseparator'},
                    {
                        text: '发布', iconCls: 'icon-database_refresh',
                        handler: function () {
                            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function(btn){
                                if (btn == 'yes') {
                                    /*
                                    var requestConfig = {
                                        url :  appPath+'/paySwitch/publish.do',
                                        callback:function(o, s, r) {
                                            var result = Ext.util.JSON.decode(r.responseText);
                                            Ext.Msg.alert('提示',result.info);
                                        }
                                    }
                                    Ext.Ajax.request(requestConfig);
                                    */
                                    Ext.Ajax.request({
                                        url: appPath + '/paySwitch/publish.do',
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
                                            App.paySwitchList.store.reload();

                                            //如果二级支付方式打开,则重新加载对应的二级支付方式列表
                                            App.paySwitchList.reloadChildPaySwitch();
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
                }, {
                    header: "支付方式",
                    width: 150,
                    sortable: true,
                    dataIndex: 'name',
                    align: 'center',
                    renderer:nameRender
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
                    width: 150,
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

            //名称渲染
            function nameRender(value, p, record){
                //1表示叶子节点(无子节点),0表示不是叶子节点(包含子节点)
                var isLeaf = record.data['isLeaf'];
                var text = value;
                if(isLeaf==0){
                    text = '<a href="javascript:void(0);" onclick="App.paySwitchList.openChildPaySwitch()">'+value+'</a>';
                }
                return text;
            }

            //状态渲染
            function statusRender(value, p, record){
                //1表示上线,0表示下线
                var status = record.data['status'];
                if(status=='1'){
                    return '上线';
                }else{
                    return '下线';
                }
            }

            //操作渲染
            function optRender(value, p, record) {
            	var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.paySwitchList.edit(' + value + ')"/>';
            	//1表示上线,0表示下线
                var status = record.data['status'];
                var statusStr = '';
                if(status=='1'){
                    statusStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_down.png" title="下线" onclick="App.paySwitchList.optStatus(' + value + ')"/>';
                }else{
                    statusStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_up.png" title="上线" onclick="App.paySwitchList.optStatus(' + value + ')"/>';
                }
                return String.format(editStr + '&nbsp;&nbsp;' + statusStr);
            }
            
            panel.add(this.grid);

        },

        //支付开关信息编辑
        edit: function() {
            if (App.paySwitchList.grid.getSelectionModel().hasSelection()) {
                App.paySwitchList.dlg.setTitle("编辑支付开关");
                var rec = App.paySwitchList.grid.getSelectionModel().getSelected();

                //处理支付类型的值,如果数据库为空,在页面上会变为0
                var payTypeId = rec.data.payTypeId;
                if(null==payTypeId || payTypeId<1){
                    payTypeId = '';
                }

                Ext.apply(App.paySwitchList.currentFormValues, {
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
                    payTypeId: payTypeId,
                    tips: Ext.util.Format.htmlDecode(rec.data.tips)
                });

                //编辑时平台信息不允许修改
                App.paySwitchList.frm.form.findField("platformId").disable();

                //如果原来不区分渠道,则"渠道号"项为disabled,否则为enabled
                if(rec.data.isDitch==0){
                    App.paySwitchList.frm.form.findField("ditchValues").disable();
                }else{
                    App.paySwitchList.frm.form.findField("ditchValues").enable();
                }

                /*
                //存在二级支付的一级支付不允许修改其版本、是否分渠道、渠道号
                if(rec.data.isLeaf==0 && App.paySwitchList.isExistChild(rec.data.id)){
                    App.paySwitchList.frm.form.findField("versionValues").setReadOnly(true);
                    App.paySwitchList.frm.form.findField("isDitch").setReadOnly(true);
                    App.paySwitchList.frm.form.findField("ditchValues").setReadOnly(true);
                }else{
                    App.paySwitchList.frm.form.findField("versionValues").setReadOnly(false);
                    App.paySwitchList.frm.form.findField("isDitch").setReadOnly(false);
                    App.paySwitchList.frm.form.findField("ditchValues").setReadOnly(false);
                }
                */

                //(非叶子)一级支付下当前存在二级支付 或 (叶子)一级支付开关下当前存在支付配置,则"isLeaf"不可编辑
                if((rec.data.isLeaf==0 && App.paySwitchList.isExistChild(rec.data.id))
                    || (rec.data.isLeaf==1 && App.paySwitchList.isExistPayConfig(rec.data.id, 0))){
                    App.paySwitchList.frm.form.findField("isLeaf").setReadOnly(true);
                }else{
                    App.paySwitchList.frm.form.findField("isLeaf").setReadOnly(false);
                }

                //如果当前为包含二级支付的一级支付,且其状态为下线,则status项为readonly
                if(rec.data.isLeaf==0 && rec.data.status==0){
                    App.paySwitchList.frm.form.findField("status").setReadOnly(true);
                }else{
                    App.paySwitchList.frm.form.findField("status").setReadOnly(false);
                }

                //如果当前支付是父节点,则不需要指定支付类型,允许为空且该项disabled
                if(rec.data.isLeaf==0){
                    App.paySwitchList.frm.form.findField("payTypeId").disable();
                }else{
                    App.paySwitchList.frm.form.findField("payTypeId").enable();
                }

                App.paySwitchList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的支付开关。');
            }
        },

        //增加一级支付
        add: function () {
            Ext.apply(App.paySwitchList.currentFormValues, {
                id: "",
                parentId: 0,
                isUse: 0,//是否发布
                name: "",
                platformId: "",
                versionValues: "",
                isDitch: "",
                ditchValues: "",
                orderId: "",
                status: "",
                isLeaf: "",
                payTypeId: "",
                tips: ""
            });
            App.paySwitchList.dlg.setTitle("增加支付开关");

            //有可能在编辑时被设置成disabled,所以这里先还原
            App.paySwitchList.frm.form.findField("ditchValues").enable();
            App.paySwitchList.frm.form.findField("platformId").enable();

            //有可能在编辑时因为存在二级支付而被设置成readonly,所以这里还原
            App.paySwitchList.frm.form.findField("versionValues").setReadOnly(false);
            App.paySwitchList.frm.form.findField("isDitch").setReadOnly(false);
            App.paySwitchList.frm.form.findField("ditchValues").setReadOnly(false);
            App.paySwitchList.frm.form.findField("isLeaf").setReadOnly(false);

            //有可能之前在增加时选中"有二级支付"而将状态设为下线且readonly,在这里还原
            App.paySwitchList.frm.form.findField("status").setReadOnly(false);

            //有可能在之前在增加或修改时支付类型变为disabled,所以需要在这里还原
            App.paySwitchList.frm.form.findField("payTypeId").enable();

            App.paySwitchList.dlg.show();
        },

        //上线/下线
        optStatus:function(){
            if (App.paySwitchList.grid.getSelectionModel().hasSelection()) {
                var rec = App.paySwitchList.grid.getSelectionModel().getSelected();
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

                        if(statusValue == 1){//当前是上线操作
                            //如果存在二级支付开关,则必须有已上线的二级支付开关,才允许对一级支付开关进行上线操作
                            if(rec.data.isLeaf==0 && !App.paySwitchList.isExistOnLineChild(id)){
                                Ext.Msg.alert('信息', '['+name+']没有已上线的二级支付开关，不能进行上线操作！');
                                return;
                            }
                            /*
                            //如果不存在二级支付开关,则必须有已上线的支付配置,才允许对一级支付开关进行上线操作
                            if(rec.data.isLeaf==1 && !App.paySwitchList.isExistOnLinePayConfig(rec.data.parentId, id)){
                                Ext.Msg.alert('信息', '['+name+']没有已上线的支付配置，不能进行上线操作！');
                                return;
                            }
                            */
                        }

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
                                App.paySwitchList.store.reload();

                                //一级支付的上下线会影响支付配置页面搜索区域和新增/修改Form中一级下拉框的值,
                                //所以如果支付配置打开,则重新加载对应的两个store
                                App.paySwitchList.reloadPayConfigOptionStore();
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
                Ext.Msg.alert('信息', '请选择要操作的支付开关。');
            }
        },

        //打开二级支付的入口
        openChildPaySwitch: function() {
            //一级支付开关ID
            var rec = App.paySwitchList.grid.getSelectionModel().getSelected();
            var id = rec.data.id;
            var platformId = rec.data.platformId;
            var versionValues = rec.data.versionValues;
            var ditchValues = rec.data.ditchValues;
            var isDitch = rec.data.isDitch;

            //如果事先已存在"二级支付开关"窗口,则得手动调用init方法
            if (App.mainPanel.getItem('childPaySwitchList')) {
                var params = {parentId: id, platformId:platformId, versionValues:versionValues, ditchValues:ditchValues, isDitch:isDitch};
                App.childPaySwitchList.init(params);
                var tab = App.mainPanel.getItem('childPaySwitchList');
                App.mainPanel.setActiveTab(tab);
            } else {
                var node = {attributes: {url: "childPaySwitchList"}, text: "二级支付开关", params: {parentId: id, platformId:platformId, versionValues:versionValues, ditchValues:ditchValues, isDitch:isDitch}};
                App.mainPanel.openTab(node);
            }
        },

        //校验一级支付下当前是否存在二级支付,如果存在二级支付则不允许修改版本、是否分渠道、渠道号
        isExistChild: function(id) {
            //为数据统一考虑,默认存在二级支付
            var result = true;

            if(null!=id && id!=undefined){
                var obj;
                if (window.ActiveXObject) {
                	obj = new ActiveXObject('Microsoft.XMLHTTP');
                }else if(window.XMLHttpRequest) {
                	obj = new XMLHttpRequest();
                }
                var url = appPath + '/paySwitch/isExistChild.do?id='+id;

                obj.open('GET', url, false);
                obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                obj.send(null);
                var value = Ext.util.JSON.decode(obj.responseText);
                if (value.success == false) {
                    result = false;
                }
            }
            return result;
        },

        //校验一级支付开关下是否存在已上线的二级支付开关
        isExistOnLineChild: function(id) {
            //为数据统一考虑,默认不存在已上线的二级支付
            var result = false;

            if(null!=id && id!=undefined){
                var obj;
                if (window.ActiveXObject) {
                    obj = new ActiveXObject('Microsoft.XMLHTTP');
                }else if(window.XMLHttpRequest) {
                    obj = new XMLHttpRequest();
                }
                var url = appPath + '/paySwitch/isExistOnLineChild.do?id='+id;

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

        //校验一级支付下当前是否存在支付配置,如果存在则不允许修"isLeaf"
        isExistPayConfig: function(parentId, id) {
            //为数据统一考虑,默认存在支付配置
            var result = true;

            if(null!=parentId && parentId!=undefined
                && null!=id && id!=undefined){

                var obj;
                if (window.ActiveXObject) {
                    obj = new ActiveXObject('Microsoft.XMLHTTP');
                }else if(window.XMLHttpRequest) {
                    obj = new XMLHttpRequest();
                }
                var url = appPath + '/payConfig/isExistPayConfig.do?parentPaySwitchId=' + parentId + '&childPaySwitchId='+id;

                obj.open('GET', url, false);
                obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                obj.send(null);
                var value = Ext.util.JSON.decode(obj.responseText);
                if (value.success == false) {
                    result = false;
                }
            }
            return result;
        },

        /*
        //校验(叶子)一级支付开关下是否存在已上线的支付配置
        isExistOnLinePayConfig: function(parentId, id) {
            //为数据统一考虑,默认不存在已上线的支付配置
            var result = false;

            if(null!=parentId && parentId!=undefined
                && null!=id && id!=undefined){

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

        //校验一级支付方式是否存在相同的名称
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

        //公共的Form保存方法
        commSave: function(){
            this.frm.form.submit({
                //防止空白框提示信息提交到后台
                submitEmptyText: false,
                success: function (form, action) {
                    Ext.Msg.alert('提示:', action.result.info);
                    form.reset();
                    App.paySwitchList.store.reload();

                    //如果二级支付方式打开,则重新加载对应的二级支付方式列表
                    App.paySwitchList.reloadChildPaySwitch();

                    //一级支新增或修改会影响支付配置页面搜索区域和新增/修改Form中一级下拉框的值,
                    //所以如果支付配置打开,则重新加载对应的两个store
                    App.paySwitchList.reloadPayConfigOptionStore();

                    App.paySwitchList.dlg.hide();
                },
                failure: function (form, action) {
                    Ext.Msg.alert('提示:', action.result.info);
                },
                waitMsg: '正在保存数据，稍后...'
            });
        },

        //校验包含二级支付的一级支付方式的版本和渠道信息是否发生变化
        isChange: function(id, versionValues, isDitch, ditchValues){
            var obj;
            if (window.ActiveXObject) {
                obj = new ActiveXObject('Microsoft.XMLHTTP');
            }else if(window.XMLHttpRequest) {
                obj = new XMLHttpRequest();
            }
            var url = appPath + '/paySwitch/isChange.do?id='+id + '&versionValues='+versionValues+'&isDitch='+isDitch + '&ditchValues='+ditchValues;

            obj.open('GET', url, false);
            obj.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            obj.send(null);
            var value = Ext.util.JSON.decode(obj.responseText);

            var info = "";
            var temp = "";

            if(value.success){
                //版本发生变化
                if(value.delVersionInfo.length>0){
                    temp = temp + "　删除的版本：" + value.delVersionInfo + "<br/>";
                }
                if(value.addVersionInfo.length>0){
                    temp = temp + "　新增的版本：" + value.addVersionInfo + "<br/>";
                }
                if(temp.length > 0){
                    info = info + "版本信息发生变化:<br/>" + temp;
                    temp = "";
                }


                /*变化类型：1从"不区分渠道号"-->"区分渠道号" 2从"区分渠道号"-->"不区分渠道号"
                 *        3修改前后都为"区分渠道号"(需求确认前后是否发生变化) 0修改前后都为"不区分渠道号",即默认没发生变化
                 */
                if(value.ditchChangeType==1){
                    temp = temp + "　支付方式从[不区分渠道号]变为[区分渠道号]<br/>";
                }else if(value.ditchChangeType==2){
                    temp = temp + "　支付方式从[区分渠道号]变为[不区分渠道号]<br/>";
                }else if(value.ditchChangeType==3){
                    //前后区分渠道号,并且前后的渠道号发生了变化
                    if(value.delDitchInfo.length>0){
                        temp = temp + "　删除的渠道号：" + value.delDitchInfo + "<br/>";
                    }
                    if(value.addDitchInfo.length>0){
                        temp = temp + "　新增的渠道号：" + value.addDitchInfo + "<br/>";
                    }
                }

                if(temp.length > 0){
                    info = info + "渠道信息发生变化:<br/>" + temp;
                }
            }

            return info;
        },

        //重新加载二级支付方式列表
        reloadChildPaySwitch:function(){
            if(App.mainPanel.getItem('childPaySwitchList')) {
                App.childPaySwitchList.store.reload();
            }
        },

        //刷新支付配置的下拉框
        reloadPayConfigOptionStore:function(){
            if(App.mainPanel.getItem('payConfigList')) {
                App.payConfigList.parentPaySwitchOptionStore.reload();
                App.payConfigList.parentPaySwitchOptionStore_Search.reload();
            }
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

        destroy: function(){
            this.store.destroy();
            this.frm.destroy();
            this.dlg.destroy();
        }
    };
}();
