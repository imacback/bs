App.batchList = function () {
    return {

        currentFormValues: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: true,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/batch/list.do'
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
                            name: "isUse",
                            type: 'int'
                        },
                        {
                            name: "providerId",
                            type: 'int'
                        },
                        {
                            name: 'providerName',
                            allowBlank: false
                        },
                        {
                            name: 'contractId',
                            allowBlank: false
                        },
                        {
                            name: "bookCount",
                            type: 'int'
                        },
                        {
                            name: "saveCount",
                            type: 'int'
                        },
                        {
                            name: "onlineCount",
                            type: 'int'
                        },
                        {
                            name: "offlineCount",
                            type: 'int'
                        },
                        {
                            name: "delCount",
                            type: 'int'
                        },
                        {
                            name: 'platformIds',
                            allowBlank: false
                        },
                        {
                            name: 'platformNames',
                            allowBlank: false
                        },
                        {
                            name: "divide",
                            allowBlank: false
                        },
                        {
                            name: "authorizeStartDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "authorizeEndDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "creatorName",
                            allowBlank: false
                        },
                        {
                            name: "editDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: "editorName",
                            allowBlank: false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        providerId: Ext.getCmp("batchList.providerId").getValue()
                    }
                );
            });

            return store;
        },

        getPlatformStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    isUse: 1
                },
                autoLoad: {},
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/platform/list.do'
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
                            name: "name",
                            allowBlank: false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams
                );
            });

            return store;
        },

        getForm: function () {
            var frm = new Ext.form.FormPanel({
                fileUpload: true,
                url: appPath + '/batch/save.do',
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
                    {//用于区分操作类型,默认为1,0新增 1修改,修改时在保存时会校验状态的变更限制
                        xtype: 'hidden',
                        name: 'opeType',
                        value: 0
                    },
                    new Ext.form.ComboBox({
                        name: 'providerId',
                        hiddenName: 'providerId',
                        fieldLabel: '版权名称',
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: { isUse: 1 },
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/provider/list.do'
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
                        anchor: '90%',
                        editable: false,
                        allowBlank: false
                    }),
                    {
                        name: 'contractId',
                        fieldLabel: '合同编号',
                        maxlength: 25,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("shortName");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    {
                        xtype: 'fieldset',
                        id: 'batchList.platformFieldSet',
                        fieldLabel: '投放平台',
                        labelWidth: 80,
                        collapsible: false,
                        autoHeight: true,
                        layout: 'column',
                        style: 'border:0px;padding:0px;margin:0px;padding-top:0px;padding-left:2px;',
                        defaults: {
                            border: false,
                            anchor: '90%'
                        },
                        defaultType: 'textfield',
                        items: []
                    },
                    {
                        name: 'divide',
                        fieldLabel: '分成比例',
                        maxlength: 25,
                        allowBlank: true,
                        listeners: {
                            scope: this,
                            keypress: function (field, e) {
                                if (e.getKey() == 13) {
                                    var obj = this.frm.form.findField("memo");
                                    if (obj) obj.focus();
                                }
                            } //keypress
                        }
                    },
                    new Ext.ux.form.DateTimeField({
                        fieldLabel: '授权开始时间',
                        name: 'authorizeStartDate',
                        id: 'batchList.authorizeStartDate',
                        emptyText: '请选择',
                        format: 'Y-m-d H:i:s',
                        listeners: {
                            'select': function(datetitmepicker, date) {
                                var value = App.batchList.formatDate(date);
                                Ext.getCmp('batchList.authorizeStartDate').setValue(value);
                            }
                        },
                        enableKeyEvents: true
                    }),
                    new Ext.ux.form.DateTimeField({
                        fieldLabel: '授权结束时间',
                        name: 'authorizeEndDate',
                        id: 'batchList.authorizeEndDate',
                        emptyText: '请选择',
                        format: 'Y-m-d H:i:s',
                         listeners: {
                            'select': function(datetitmepicker, date) {
                                var value = App.batchList.formatDate(date);
                                Ext.getCmp('batchList.authorizeEndDate').setValue(value);

                            }
                        },
                        enableKeyEvents: true
                    }),
                    new Ext.form.ComboBox({
                        hiddenName: 'isUse',
                        fieldLabel: '状态',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['正常', '1'],
                                ['终止', '0']
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
                        fieldLabel: 'Excel文件',
                        name: 'file',
                        id: 'batchList.fileUpload',
                        width: 400,
                        emptyText: '选择Excel文件(内容必须为文本格式)...',
                        //regex: /^[^\u4e00-\u9fa5]*$/,
                        //regexText: '文件名中不能包含中文',
                        regex:/^.*\.(?:xls|xlsx)$/,
                        regexText: '上传的文件必须为Excel文件',
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
                        handler: function () {
                            if (this.frm.form.isValid()) {

                                //当前的操作类型,0新增,1修改
                                var opeType = this.frm.form.findField("opeType").getValue();
                                //只有edit时才需要做校验
                                if(null!=opeType && ''!=opeType && undefined!=opeType && opeType==1){
                                    //限制已终止的批次的逆向操作
                                    var oldIsUse = App.batchList.grid.getSelectionModel().getSelected().data.isUse;
                                    var currentIsUse = this.frm.form.findField("isUse").getValue();
                                    if(oldIsUse=='0' && currentIsUse=='1'){
                                        Ext.Msg.alert('提示:', '已终止的批次不能变为正常状态！');
                                        return;
                                    }
                                }

                                this.frm.form.submit({
                                    success: function (form, action) {
                                        Ext.Msg.alert('提示:', action.result.info);
                                        form.reset();
                                        App.batchList.store.reload();
                                        App.batchList.dlg.hide();
                                    },
                                    failure: function (form, action) {
                                        Ext.Msg.alert('提示:', '<div style="width:300px;height:120px;overflow:auto;"><font>' + action.result.info + '</font></div>');
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
                width: 400,
                height: 330,
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
                    },
                    //点击窗口右上角的"X"后重新对form进行reset,否则之前上传的附件(file项)还会生效,即使在打开编辑窗口时设置了file为空值
                    hide:function(){
                        this.frm.form.reset();
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
                    '版权名称：',
                    new Ext.form.ComboBox({
                        id: "batchList.providerId",
                        store: new Ext.data.Store({
                            autoLoad: {},
                            baseParams: { isUse: 1 },
                            proxy: new Ext.data.HttpProxy({
                                url: appPath + '/provider/list.do'
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
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.batchList.store.load();
                        }
                    }
                ]
            });

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    {
                        xtype: 'button',
                        text: '新增',
                        iconCls: 'icon-add',
                        handler: this.add
                    }
                ],
                store: this.store,
                columns: [
                    {
                        header: "序号",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "版权名称",
                        width: 120,
                        sortable: true,
                        dataIndex: 'providerName',
                        align: 'center'
                    },
                    {
                        header: "合同编号",
                        width: 150,
                        sortable: true,
                        dataIndex: 'contractId',
                        align: 'center'
                    },
                    {
                        header: "书籍总数量",
                        width: 90,
                        sortable: true,
                        dataIndex: 'bookCount',
                        align: 'center'
                    },
                    {
                        header: "书籍入库数量",
                        width: 90,
                        sortable: true,
                        dataIndex: 'saveCount',
                        align: 'center'
                    },
                    {
                        header: "书籍上线数量",
                        width: 90,
                        sortable: true,
                        dataIndex: 'onlineCount',
                        align: 'center'
                    },
                    {
                        header: "书籍下线数量",
                        width: 90,
                        sortable: true,
                        dataIndex: 'offlineCount',
                        align: 'center'
                    },
                    /*
                    {
                        header: "书籍删除数量",
                        width: 100,
                        sortable: true,
                        dataIndex: 'delCount',
                        align: 'center'
                    },
                    */
                    {
                        header: "默认运营平台",
                        width: 150,
                        sortable: true,
                        dataIndex: 'platformNames',
                        align: 'center'
                    },
                    {
                        header: "授权开始时间",
                        width: 90,
                        sortable: true,
                        dataIndex: 'authorizeStartDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },
                    {
                        header: "授权结束时间",
                        width: 90,
                        sortable: true,
                        dataIndex: 'authorizeEndDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },
                    {
                        header: "状态",
                        width: 60,
                        sortable: true,
                        dataIndex: 'isUse',
                        align: 'center',
                        renderer: App.statusRender
                    }, {
                        header: "分成比例",
                        width: 60,
                        sortable: true,
                        dataIndex: 'divide',
                        align: 'center'
                    },
                    {
                        header: "操作",
                        width: 60,
                        sortable: false,
                        dataIndex: 'id',
                        align: 'center',
                        renderer: optRender
                    }
                ],
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
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.batchList.edit(' + value + ')"/>');
            }

            panel.add(this.grid);

        },

        add: function () {
            App.batchList.buildCheckGroup('');
            Ext.apply(App.batchList.currentFormValues, {
                id: '',
                providerId: "",
                contractId: "",
                platformIds: "",
                divide: 0,
                isUse:1,
                opeType:0,//用于标识add操作
                authorizeOnDate:"" ,
                authorizeOffDate:"",
                file:""
            });

            App.batchList.dlg.setTitle("增加批次");
            App.batchList.dlg.show();
            Ext.getCmp('batchList.authorizeEndDate').setValue(App.batchList.formatDate(new Date()));
            Ext.getCmp('batchList.authorizeStartDate').setValue(App.batchList.formatDate(new Date()));
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

        edit: function () {
            if (App.batchList.grid.getSelectionModel().hasSelection()) {
                App.batchList.dlg.setTitle("编辑批次");
                var rec = App.batchList.grid.getSelectionModel().getSelected();
                App.batchList.buildCheckGroup(rec.data.platformIds);
                Ext.apply(App.batchList.currentFormValues, {
                    id: rec.data.id,
                    providerId: rec.data.providerId,
                    contractId: rec.data.contractId,
                    platformIds: rec.data.platformIds,
                    divide: rec.data.divide,
                    isUse: rec.data.isUse,
                    opeType:1,//用于标识edit操作
                    authorizeStartDate: rec.data.authorizeStartDate,
                    authorizeEndDate: rec.data.authorizeEndDate,
                    file:""
                });
                App.batchList.dlg.show();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的批次。');
            }
        },

        buildCheckGroup: function (values) {
            var ids = values.split(',');
            var length = ids.length;
            var size = this.platformStore.getTotalCount();
            var fieldSet = Ext.getCmp("batchList.platformFieldSet");
            fieldSet.removeAll();

            for (var i = -1; i < size; i++) {
                var moduleId = '';
                var moduleName = '';
                var checkboxId = '';
                var checked = false;
                var hidden = true;

                if (i > -1) {
                    hidden = false;
                    checkboxId = i;
                    moduleId = this.platformStore.getAt(i).data.id;
                    moduleName = this.platformStore.getAt(i).data.name;
                    if (values != '') {
                        for (var j = 0; j < length; j++) {
                            if (ids[j] == moduleId) {
                                checked = true;
                            }
                        }
                    } else {
                        checked = true;
                    }
                }

                var checkboxModule = new Ext.form.Checkbox({
                    checked: checked,
                    width: 90,
                    id: "boxModule_" + checkboxId,
                    name: "platformIds",
                    boxLabel: moduleName,
                    labelSeparator: "",  // 当没有标题时，不要 “:” 号，不要标题分隔
                    inputValue: moduleId,
                    hidden: hidden
                });

                fieldSet.add(checkboxModule); // 这里我是一个 fieldset ，你也可以是一个 form 或者别的东西。
            }
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            }
            if (!this.platformStore) {
                this.platformStore = this.getPlatformStore();
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

        destroy: function() {
            this.store.destroy();
            this.platformStore.destroy();
            this.frm.destroy();
            this.dlg.destroy();
        }
    };
}();
