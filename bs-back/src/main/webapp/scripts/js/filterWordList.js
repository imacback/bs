App.filterWordList = function() {
    return {

        currentFormValues: {},

        getStore: function() {
            var store = new Ext.data.Store({
                baseParams : {
                    start : App.start_page_default,
                    limit : App.limit_page_default
                },
                autoLoad : true,
                proxy: new Ext.data.HttpProxy({
                    url : appPath+'/filterWord/list.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty : 'totalItems',
                    root : 'result',
                    idProperty : 'id',
                    fields: [
                        {
                            name: 'id',
                            type: 'int'
                        },
                        {
                            name: 'level',
                            type: 'int'
                        },
                        {
                            name: 'status',
                            type: 'int'
                        },
                        {
                            name: 'replaceStrategyType',
                            type: 'int'
                        },
                        {
                            name: 'word',
                            allowBlank: false
                        },
                        {
                            name: 'replaceWord',
                            allowBlank: false
                        },
                        {
                            name: 'allowComment',
                            type: 'int'
                        },
                        {
                            name: 'creatorName',
                            allowBlank: false
                        },
                        {
                            name: "createDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: 'editorName',
                            allowBlank: false
                        },
                        {
                            name: "editDate",
                            type: 'date',
                            dateFormat: 'time'
                        }]
                })
            });

            store.on('beforeload', function(thiz,options) {
                Ext.apply(
                    thiz.baseParams, {
                        word : Ext.getCmp('filterWordList.word').getValue(),
                        status : Ext.getCmp('filterWordList.status').getValue()
                    });
            });

            return store;
        },

        getForm: function() {
            var frm = new Ext.form.FormPanel({
                url : appPath+'/filterWord/save.do',
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
                        name: 'word',
                        fieldLabel: '敏感词',
                        maxLength: 50
                        //regex: /^[0-9a-zA-Z\u4E00-\u9FA5]+$/,
                        //regexText: '只能输入25个数字英文及汉字',
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'level',
                        fieldLabel: '等级',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['b', '10'],
                                ['a', '20'],
                                ['s', '30']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    new Ext.form.ComboBox({
                        hiddenName: 'replaceStrategyType',
                        fieldLabel: '替换策略',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['不替换', '0'],
                                ['替换为*', '1'],
                                ['替换为替换词', '2']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        listeners: {
                            select: App.filterWordList.changeReplaceStrategyType
                        }
                    }),
                    {
                        name: 'replaceWord',
                        fieldLabel: '替换词',
                        allowBlank: true,
                        maxLength: 50
                        //regex: /^[0-9a-zA-Z\u4E00-\u9FA5]+$/,
                        //regexText: '只能输入25个数字英文及汉字',
                    },
                    new Ext.form.ComboBox({
                        hiddenName: 'allowComment',
                        fieldLabel: '可发评论',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['是', '1'],
                                ['否', '0']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    new Ext.form.ComboBox({
                        hiddenName: 'status',
                        fieldLabel: '状态',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['禁用', '0'],
                                ['可用', '1']
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
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-save',
                    scope: this,
                    handler: function() {
                        if (this.frm.form.isValid()) {
                            var type = this.frm.form.findField("replaceStrategyType").getValue();
                            var replaceWord = this.frm.form.findField("replaceWord");
                            if (type == 2 && replaceWord == '') {
                                Ext.Msg.alert('提示:', '请输入替换词');
                                return;
                            }
                            this.frm.form.submit({
                                success : function(form, action) {
                                    Ext.Msg.alert('提示:', action.result.info);
                                    form.reset();
                                    App.filterWordList.store.reload();
                                    App.filterWordList.dlg.hide();
                                },
                                failure : function(form, action) {
                                    Ext.Msg.alert('提示:',action.result.info);
                                },
                                waitMsg : '正在保存数据，稍后...'
                            });
                        }
                    }
                },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function() {
                            this.frm.form.reset();
                            this.frm.form.clearInvalid();
                        }
                    }] //buttons
            }); //FormPanel

            return frm;
        },

        getMultiAddForm: function() {
            var mfrm = new Ext.form.FormPanel({
                url : appPath+'/filterWord/multiAdd.do',
                fileUpload: true,
                labelAlign: 'left',
                buttonAlign: 'center',
                bodyStyle: 'padding:5px',
                frame: true,
                labelWidth: 80,
                defaultType: 'textfield',
                defaults: {
                    allowBlank: true,
                    anchor: '90%',
                    enableKeyEvents: true
                },
                items: [
                    new Ext.form.ComboBox({
                        hiddenName: 'level',
                        fieldLabel: '等级',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['a', '10'],
                                ['b', '20'],
                                ['s', '30']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    new Ext.form.ComboBox({
                        hiddenName: 'replaceStrategyType',
                        fieldLabel: '替换策略',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['不替换', '0'],
                                ['替换为*', '1'],
                                ['替换为替换词', '2']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false
                    }),
                    {
                        xtype: 'textarea',
                        name: 'words',
                        fieldLabel: '敏感词',
                        emptyText: '多个敏感词通过“,”号分隔',
                        maxLength: 1024,
                        height: 120
                        //regex: /^[0-9a-zA-Z\u4E00-\u9FA5]+$/,
                        //regexText: '只能输入25个数字英文及汉字',
                    },
                    {
                        xtype: 'fileuploadfield',
                        fieldLabel: 'excel文件',
                        name: 'file',
                        id: 'filterWordList.fileUpload',
                        width: 400,
                        emptyText: '选择excel文件...',
                        regex: /\.([xX][lL][sS]){1}$|\.([xX][lL][sS][xX]){1}$/,
                        regexText: '请上传xls或xlsx格式文件',
                        buttonText: '',
                        buttonCfg: {
                            iconCls: 'icon-search'
                        }
                    }
                ],
                //items
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-save',
                    scope: this,
                    handler: function() {
                        if (this.mfrm.form.isValid()) {
                            var file = this.mfrm.form.findField("file").getValue();
                            if (file == '') {
                                var level = this.mfrm.form.findField("level").getValue();
                                if (level == '') {
                                    Ext.Msg.alert('提示:', '请选择级别');
                                    return;
                                }
                                var replaceStrategyType = this.mfrm.form.findField("replaceStrategyType").getValue();
                                if (replaceStrategyType == '') {
                                    Ext.Msg.alert('提示:', '请选择替换策略');
                                    return;
                                }
                                var words = this.mfrm.form.findField("words").getValue();
                                if (words == '') {
                                    Ext.Msg.alert('提示:', '请输入敏感词');
                                    return;
                                }
                            }

                            this.mfrm.form.submit({
                                success : function(form, action) {
                                    Ext.Msg.alert('提示:', action.result.info);
                                    form.reset();
                                    App.filterWordList.store.reload();
                                    App.filterWordList.mdlg.hide();
                                },
                                failure : function(form, action) {
                                    Ext.Msg.alert('提示:',action.result.info);
                                },
                                waitMsg : '正在保存数据，请稍后...'
                            });
                        }
                    }
                },
                    {
                        text: '重置',
                        iconCls: 'icon-cross',
                        scope: this,
                        handler: function() {
                            this.mfrm.form.reset();
                            this.mfrm.form.clearInvalid();
                        }
                    }] //buttons
            }); //FormPanel

            return mfrm;
        },

        getDialog: function() {
            var dlg = new Ext.Window({
                width: 400,
                height: 240,
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
                    render: function(fp) {
                        this.frm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function() {
                        this.frm.form.setValues(this.currentFormValues);
                        this.frm.form.clearInvalid();
                    }
                }
            }); //dlg
            return dlg;
        },

        getMultiAddDialog: function() {
            var mdlg = new Ext.Window({
                width: 400,
                height: 280,
                title: '',
                plain: true,
                closable: true,
                resizable: false,
                frame: true,
                layout: 'fit',
                closeAction: 'hide',
                border: false,
                modal: true,
                items: [this.mfrm],
                listeners: {
                    scope: this,
                    render: function(fp) {
                        this.mfrm.form.waitMsgTarget = fp.getEl();
                    },
                    show: function() {
                        this.mfrm.form.clearInvalid();
                    }
                }
            }); //dlg
            return mdlg;
        },

        createGrid: function(id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            var sm = new Ext.grid.CheckboxSelectionModel();

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items:[
                    {xtype : 'tbtext', text:'关键字：'},
                    {
                        xtype: 'textfield',
                        id: 'filterWordList.word',
                        width: 100
                    },
                    {xtype : 'tbseparator'},
                    {xtype : 'tbtext', text:'状态：'},
                    new Ext.form.ComboBox({
                        id: 'filterWordList.status',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['可用', '1'],
                                ['禁用', '0'],
                                ['全部', '']
                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        width: 100
                    }),
                    {xtype : 'tbseparator'},
                    {
                        xtype : 'button',
                        text:'查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.filterWordList.store.load();
                        }
                    },
                    {xtype : 'tbseparator'},
                    {
                        xtype : 'button',
                        text:'导出',
                        iconCls: 'icon-page_excel',
                        handler: function () {
                            App.filterWordList.exportExcel()
                        }
                    }
                ]
            });

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    {
                        text: '新增',
                        scope: this,
                        iconCls: 'icon-add',
                        handler: this.add
                    },
                    {xtype : 'tbseparator'},
                    {
                        text: '删除',
                        scope: this,
                        iconCls: 'icon-delete',
                        handler: this.del
                    },
                    {xtype : 'tbseparator'},
                    {
                        text: '发布', iconCls: 'icon-database_refresh',
                        handler: function () {
                            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function(btn){
                                if (btn == 'yes') {
                                    var requestConfig = {
                                        url :  appPath+'/filterWord/publish.do',
                                        callback:function(o, s, r) {
                                            var result = Ext.util.JSON.decode(r.responseText);
                                            Ext.Msg.alert('提示',result.info);
                                        }
                                    }
                                    Ext.Ajax.request(requestConfig);
                                }
                            });
                        }
                    },
                ],
                store: this.store,
                sm: sm,
                columns: [sm, {
                    header: "ID",
                    width: 60,
                    sortable: true,
                    dataIndex: 'id',
                    align: 'center'
                }, {
                    header: "敏感词",
                    width: 300,
                    sortable: true,
                    dataIndex: 'word',
                    align: 'center'
                }, {
                    header: "级别",
                    width: 60,
                    sortable: true,
                    dataIndex: 'level',
                    align: 'center',
                    renderer: levelRender
                }, {
                    header: "替换策略",
                    width: 80,
                    sortable: true,
                    dataIndex: 'replaceStrategyType',
                    align: 'center',
                    renderer: replaceRender
                }, {
                    header: "替换词",
                    width: 100,
                    sortable: true,
                    dataIndex: 'replaceWord',
                    align: 'center'
                }, {
                    header: "可发评论",
                    width: 60,
                    sortable: true,
                    dataIndex: 'allowComment',
                    align: 'center',
                    renderer: App.statusRender
                }, {
                    header: "状态",
                    width: 60,
                    sortable: true,
                    dataIndex: 'status',
                    align: 'center',
                    renderer: App.statusRender
                }, {
                    header: "操作",
                    width: 80,
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
                    store: this.store,
                    pageSize: this.store.baseParams.limit,
                    plugins: [new Ext.ux.PageSizePlugin()],
                    displayInfo: true,
                    emptyMsg: '没有找到相关数据'
                })
            });

            function optRender(value, p, record) {
                return String.format('<input type="image" src="'+appPath+'/scripts/icons/page_edit.png" title="编辑" onclick="App.filterWordList.edit(\''+value+'\')"/>');
            }

            function levelRender(value, p, record) {
                var name;
                if (value == 10) {
                    name = 'b';
                } else if (value == 20) {
                    name = 'a';
                } else if (value == 30) {
                    name = 's';
                }

                return String.format(name);
            }

            function replaceRender(value, p, record) {
                var name;
                if (value == 0) {
                    name = '不替换';
                } else if (value == 1) {
                    name = '替换为*';
                } else if (value == 2) {
                    name = '替换为替换词';
                }

                return String.format(name);
            }

            panel.add(this.grid);

        },

        add: function() {
            App.filterWordList.mfrm.form.findField("words").setValue('');
            App.filterWordList.mfrm.form.findField("level").setValue('30');
            App.filterWordList.mfrm.form.findField("replaceStrategyType").setValue('1');
            App.filterWordList.mdlg.setTitle("增加敏感词");
            App.filterWordList.mdlg.show();
        },

        edit: function() {
            if (this.grid.getSelectionModel().hasSelection()) {
                this.dlg.setTitle("编辑敏感词");
                var rec = this.grid.getSelectionModel().getSelected();

                Ext.apply(this.currentFormValues, {
                    id: rec.data.id,
                    word: rec.data.word,
                    level: rec.data.level,
                    replaceStrategyType: rec.data.replaceStrategyType,
                    status: rec.data.status,
                    allowComment: rec.data.allowComment,
                    replaceWord: rec.data.replaceWord
                });

                this.dlg.show();

                App.filterWordList.changeReplaceStrategyType();
            } else {
                Ext.Msg.alert('信息', '请选择要编辑的敏感词。');
            }
        },

        del: function () {
            if (App.filterWordList.grid.getSelectionModel().hasSelection()) {
                var recs = App.filterWordList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.id);
                    words += data.word + '<br>';
                }
                Ext.Msg.confirm('删除敏感词', '确定删除以下敏感词？<br><font color="red">' + words + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/filterWord/del.do',
                                params: {
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.filterWordList.store.load();
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
                Ext.Msg.alert('信息', '请选择要删除的敏感词！');
            }
        },

        exportExcel:  function() {
            var word = Ext.getCmp('filterWordList.word').getValue();
            var status = Ext.getCmp("filterWordList.status").getValue();

            var query = '?1=1';
            if (word != '') {
                query = query + '&word='+word;
            }
            if (status != '') {
                query = query + '&status='+status;
            }

            window.open(appPath + '/filterWord/export.do' + query);
        },

        changeReplaceStrategyType: function() {
            var type = App.filterWordList.frm.form.findField("replaceStrategyType").getValue();
            var replaceWord = App.filterWordList.frm.form.findField("replaceWord");
            if (type == 2) {
                replaceWord.enable();
            } else {
                replaceWord.setValue("");
                replaceWord.disable();
            }
        },

        render: function(id) {
            if (!this.store) {
                this.store = this.getStore();
            };
            if (!this.frm) {
                this.frm = this.getForm();
            }; //if(!this.frm)

            if (!this.mfrm) {
                this.mfrm = this.getMultiAddForm();
            }; //if(!this.frm)


            if (!this.dlg) {
                this.dlg = this.getDialog();
            }; //if(!this.dlg)

            if (!this.mdlg) {
                this.mdlg = this.getMultiAddDialog();
            }; //if(!this.dlg)

            this.createGrid(id);

            Ext.getCmp("filterWordList.status").setValue(1);

        },

        init: function(params) {},

        destroy: function() {
            this.store.destroy();
            this.frm.destroy();
            this.mfrm.destroy();
            this.dlg.destroy();
            this.mdlg.destroy();
        }
    };
} ();
