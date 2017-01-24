App.splashList = function () {
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
                    url: appPath + '/splash/list.do'
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
                        	name: 'startDate',
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: 'endDate',
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },{
                            name:'platformName',
                            type:'string'
                        },{
                            name:'imageUrl',
                            type:'string'
                        },{
                            name: 'editDate',
                            type: 'date',
                            dateFormat: 'time'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {

                var startCreateDate = App.dateFormat(Ext.getCmp("splashList.startCreateDate").getValue());
                var endCreateDate = App.dateFormat(Ext.getCmp("splashList.endCreateDate").getValue());
                if(null!=endCreateDate && ''!=endCreateDate && undefined!=endCreateDate){
                    endCreateDate = endCreateDate.replace("00:00:00","23:59:59");
                }

                Ext.apply(
                    thiz.baseParams,
                    {
                    	platformId: Ext.getCmp("splashList.platformId").getValue(),
                    	ditch: Ext.getCmp("splashList.ditch").getValue(),
                    	version: Ext.getCmp("splashList.version").getValue(),
                    	name: Ext.getCmp("splashList.name").getValue(),
                    	status: Ext.getCmp("splashList.status").getValue(),
                    	startCreateDate: startCreateDate,
                    	endCreateDate:endCreateDate
                    }
                );
            });
            return store;
        },

        //编辑启动页配置信息的Form
        getForm: function () {
            var frm = new Ext.form.FormPanel({
                //method: 'POST',
                //layout: 'form',
                fileUpload: true,
            	url: appPath + '/splash/save.do',
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
                        fieldLabel: '名称',
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
                                var dataObj = Ext.getCmp("splashList.ditchValues");
                                if(combo.getValue()=='0'){
                                    //不区分渠道时,渠道号为空且为disabled
                                    dataObj.allowBlank = true;
                                    dataObj.setValue("");;
                                    //dataObj.setReadOnly(true);
                                    dataObj.setDisabled(true);
                                }else{
                                    dataObj.allowBlank = false;
                                    //dataObj.setReadOnly(false);
                                    dataObj.setDisabled(false);
                                }
                            }
                        }
                    }),
                    {
                        name: 'ditchValues',
                        id:'splashList.ditchValues',
                        fieldLabel: '渠道号',
                        allowBlank:true,
                        emptyText: '多渠道请用;隔开,不填则表示全部渠道'
                    },
                    new Ext.ux.form.DateTimeField({
                        fieldLabel: '有效开始时间',
                        name: 'startDate',
                        id: 'splashList.startDate',
                        emptyText: '请选择',
                        format: 'Y-m-d H:i:s',
                        listeners: {
                            'select': function(datetitmepicker, date) {
                                var value = App.splashList.formatDate(date);
                                Ext.getCmp('splashList.startDate').setValue(value);
                            }
                        },
                        enableKeyEvents: true
                    }),
                    new Ext.ux.form.DateTimeField({
                        fieldLabel: '有效结束时间',
                        name: 'endDate',
                        id: 'splashList.endDate',
                        emptyText: '请选择',
                        format: 'Y-m-d H:i:s',
                         listeners: {
                            'select': function(datetitmepicker, date) {
                                var value = App.splashList.formatDate(date);
                                Ext.getCmp('splashList.endDate').setValue(value);

                            }
                        },
                        enableKeyEvents: true
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
                    }),
                    {
                        xtype: 'fileuploadfield',
                        fieldLabel: '图片',
                        name: 'file',
                        id: 'splashList.fileUpload',
                        width: 400,
                        emptyText: '选择图片文件...',
                        regex: /^[^\u4e00-\u9fa5]*$/,
                        regexText: '文件名中不能包含中文',
                        buttonText: '',
                        buttonCfg: {
                            iconCls: 'icon-search'
                        },
                        allowBlank: true
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
                                    //防止空白框提示信息提交到后台
                                    submitEmptyText: false,
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.splashList.store.reload();
                                        App.splashList.dlg.hide();
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

        //启动页
        getDialog: function () {
            var dlg = new Ext.Window({
                width: 400,
                height: 320,
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

        //启动页图片预览的form
        getPreviewForm: function () {
            var previewFrm = new Ext.form.FormPanel({
                url:'',
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:0px',
                layout: 'form',
                autoScroll: true,
                frame: true,
                items: [
                    {
                        xtype: 'box',
                        cls: 'xx-box',
                        id: 'splashList.coverShowBox',
                        height: "250",
                        width: "250",
                        autoEl: {
                            tag: 'img',
                            src:''
                        }
                    }
                ]
            });
            return previewFrm;
        },

        //启动页图片预览
        getPreviewDialog: function () {
            var previewDlg = new Ext.Window({
                width: 201,
                height: 297,
                title: '启动页图片预览',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.previewFrm],
                listeners: {
                    hide:function(panel, eopts){
                        //关闭时清空图片路径
                        Ext.getCmp("splashList.coverShowBox").getEl().dom.src='';
                    }
                }
            }); //dlg
            return previewDlg;
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
                        id: "splashList.platformId",
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
                    '渠道号：',
                    {
                        xtype: 'textfield',
                        id: 'splashList.ditch',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '版本号：',
                    {
                        xtype: 'textfield',
                        id: 'splashList.version',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '名称：',
                    {
                        xtype: 'textfield',
                        id: 'splashList.name',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    '创建开始日期',
                    {
                        id: 'splashList.startCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d',
                        editable:true
                    },
                    {xtype: 'tbseparator'},
                    '创建结束日期',
                    {
                        id: 'splashList.endCreateDate',
                        xtype: "datefield",
                        format : 'Y-m-d',
                        editable:true
                    },
                    {xtype: 'tbseparator'},
                    '状态：',
                    new Ext.form.ComboBox({
                        id: 'splashList.status',
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
                            App.splashList.store.load();
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
                    }
                       /*
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '删除',
                        iconCls: 'icon-delete',
                        handler: this.del
                    }
                        */
                ],
                store: this.store,
                sm: sm,
                columns: [sm, {
                    header: "ID",
                    width: 40,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center',
                    hideable: false
                }, {
                    header: "名称",
                    width: 150,
                    sortable: true,
                    dataIndex: 'name',
                    align: 'center'
                },{
                    header: "平台",
                    width: 80,
                    sortable: true,
                    dataIndex: 'platformName',
                    align: 'center'
                },{
                    header: "版本号",
                    width: 80,
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
                    width: 100,
                    sortable: true,
                    dataIndex: 'ditchValues',
                    align: 'center'
                },{
                    header: "有效开始时间",
                    width: 120,
                    sortable: true,
                    dataIndex: 'startDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },{
                    header: "有效结束时间",
                    width: 120,
                    sortable: true,
                    dataIndex: 'endDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')
                },{
                    header: "状态",
                    width: 60,
                    sortable: false,
                    dataIndex: 'status',
                    align: 'center',
                    renderer: statusRender
                },{
                    header: "创建日期",
                    width: 80,
                    sortable: true,
                    dataIndex: 'createDate',
                    align: 'center',
                    renderer: Ext.util.Format.dateRenderer('Y-m-d')
                },{
                    header: "编辑时间",
                    width: 120,
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
            	var editStr = '<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.splashList.edit(' + value + ')"/>';
            	//1表示上线,0表示下线
                var status = record.data['status'];
                var statusStr = '';
                if(status=='1'){
                    statusStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_down.png" title="下线" onclick="App.splashList.optStatus(' + value + ')"/>';
                }else{
                    statusStr = '<input type="image" src="' + appPath + '/scripts/icons/arrow_up.png" title="上线" onclick="App.splashList.optStatus(' + value + ')"/>';
                }
                //图片预览
                var preview = '<input type="image" src="' + appPath + '/scripts/icons/eye.png" title="图片预览" onclick="App.splashList.preview(' + value + ')"/>';
                return String.format(editStr + '&nbsp;&nbsp;' + statusStr + '&nbsp;&nbsp;' + preview);
            }
            
            panel.add(this.grid);

        },

        //启动页信息编辑
        edit: function() {
            if (App.splashList.grid.getSelectionModel().hasSelection()) {
                App.splashList.dlg.setTitle("编辑启动页资料");
                var rec = App.splashList.grid.getSelectionModel().getSelected();
                Ext.apply(App.splashList.currentFormValues, {
                    id: rec.data.id,
                    name: rec.data.name,
                    platformId: rec.data.platformId,
                    versionValues: rec.data.versionValues,
                    isDitch: rec.data.isDitch,
                    ditchValues: rec.data.ditchValues,
                    startDate: rec.data.startDate,
                    endDate: rec.data.endDate,
                    status: rec.data.status,
                    file: ""
                });
                App.splashList.frm.form.findField("platformId").disable();
                //如果原来不区分渠道,则"渠道号"项为disabled,否则为enabled
                if(rec.data.isDitch==0){
                    App.splashList.frm.form.findField("ditchValues").disable();
                }else{
                    App.splashList.frm.form.findField("ditchValues").enable();
                }
                App.splashList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的启动页面。');
            }
        },

        //增加启动页配置
        add: function () {
            Ext.apply(App.splashList.currentFormValues, {
                id: "",
                name: "",
                platformId: "",
                versionValues: "",
                isDitch: "",
                ditchValues: "",
                startDate: "",
                endDate: "",
                status: "",
                file: ""
            });
            App.splashList.dlg.setTitle("增加启动页配置");

            Ext.getCmp('splashList.startDate').setValue(App.splashList.formatDate(new Date()));
            Ext.getCmp('splashList.endDate').setValue(App.splashList.formatDate(new Date()));

            App.splashList.frm.form.findField("ditchValues").enable();
            App.splashList.frm.form.findField("platformId").enable();

            App.splashList.dlg.show();
        },

        //上线/下线
        optStatus:function(){
            if (App.splashList.grid.getSelectionModel().hasSelection()) {
                var rec = App.splashList.grid.getSelectionModel().getSelected();
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
                            url: appPath + '/splash/optStatus.do',
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
                                App.splashList.store.reload();
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
                Ext.Msg.alert('信息', '请选择要操作的启动页。');
            }
        },

        //图片预览
        preview:function(){
            if (App.splashList.grid.getSelectionModel().hasSelection()) {
                var imageUrl = App.splashList.grid.getSelectionModel().getSelected().data.imageUrl;
                App.splashList.previewDlg.show();
                //该语句必须置于show下边,否则取不到El
                Ext.getCmp("splashList.coverShowBox").getEl().dom.src = imageUrl;
            } else {
                Ext.Msg.alert('信息', '请选择要预览图片的启动页面。');
            }
        },

        formatDate: function(date) {
            var month = date.getMonth()+1;
            var monthValue = month>9?month:'0'+month;
            var day = date.getUTCDate();
            var dayValue = day>9?day:'0'+day;
            var hour = date.getHours();
            var hourValue = hour>9?hour:'0'+hour;
            return date.getFullYear()+"-"+monthValue+'-'+dayValue+' '+hourValue+':00:00';
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

            //图片预览
            if(!this.previewFrm){
                this.previewFrm = this.getPreviewForm();
            };
            if(!this.previewDlg){
                this.previewDlg = this.getPreviewDialog();
            };

            this.createGrid(id);

        },

        init: function(params) {
        },

        destroy: function() {}
    };
}();
