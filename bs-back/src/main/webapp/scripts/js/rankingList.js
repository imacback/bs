App.rankingList = function () {
    return {

        currentFormValues: {},

        getStore: function () {
            var store = new Ext.data.Store({
                baseParams: {
                    start: App.start_page_default,
                    limit: App.limit_page_default
                },
                autoLoad: false,
                proxy: new Ext.data.HttpProxy({
                    url: appPath + '/ranking/list.do'
                }),
                remoteSort: false,
                reader: new Ext.data.JsonReader({
                    totalProperty: 'totalItems',
                    root: 'result',
                    idProperty: 'id',
                    fields: [
                        {
                            name: 'id',
                            type: 'long'
                        },
                        {
                            name: 'name',
                            allowBlank: false
                        },
                        {
                            name: 'author',
                            allowBlank: false
                        },
                        {
                            name: "viewCount",
                            type: 'int'
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        listType: Ext.getCmp("rankingList.listType").getValue(),
                        dateType: Ext.getCmp("rankingList.dateType").getValue()
                    }
                );
            });

            return store;
        },

        createGrid: function (id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            var searchBar = new Ext.Toolbar({
                renderTo: Ext.grid.GridPanel.tbar,
                items: [
                    '榜单类型：',
                    new Ext.form.ComboBox({
                        id: 'rankingList.listType',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['男生原创玄幻', '1'],
                                ['男生原创都市', '2'],
                                ['女生原创古言穿越', '3'],
                                ['女生原创都市言情', '4'],
                                ['最热出版古典', '5'],
                                ['最热出版两性文学', '6']

                            ]
                        }),
                        mode: 'local',
                        displayField: 'text',
                        valueField: 'value',
                        triggerAction: 'all',
                        editable: false,
                        width: 120
                    }),
                    {xtype: 'tbseparator'},
                    '榜单周期：',
                    new Ext.form.ComboBox({
                        id: 'rankingList.dateType',
                        store: new Ext.data.SimpleStore({
                            fields: ['text', 'value'],
                            data: [
                                ['天', '1']
                                /*
                                 ,['周', '2']
                                 ,['月', '3']
                                 */
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
                            var listType = Ext.getCmp("rankingList.listType").getValue();
                            if (listType == '') {
                                Ext.Msg.alert('信息', '请选择榜单类型');
                                return;
                            }
                            var dateType = Ext.getCmp("rankingList.dateType").getValue();
                            if (dateType == '') {
                                Ext.Msg.alert('信息', '请选择榜单周期');
                                return;
                            }
                            App.rankingList.store.load();
                        }
                    }
                ]
            });

            var sm = new Ext.grid.CheckboxSelectionModel();

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    {
                        text: '发布榜单', iconCls: 'icon-database_refresh',
                        handler: function () {
                            Ext.MessageBox.confirm('请确认', '您确认执行发布操作吗？', function(btn){
                                if (btn == 'yes') {
                                    var requestConfig = {
                                        url :  appPath+'/ranking/publish.do',
                                        callback:function(o, s, r) {
                                            var result = Ext.util.JSON.decode(r.responseText);
                                            Ext.Msg.alert('提示',result.info);
                                        }
                                    }
                                    Ext.Ajax.request(requestConfig);
                                }
                            });
                        }
                    }
                ],
                store: this.store,
                sm: sm,
                columns: [//sm,
                    {
                        header: "书ID",
                        width: 80,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center'
                    },
                    {
                        header: "书名",
                        width: 200,
                        sortable: true,
                        dataIndex: 'name',
                        align: 'center'
                    },
                    {
                        header: "真实数据",
                        width: 120,
                        sortable: true,
                        dataIndex: 'viewCount',
                        align: 'center'
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

            /*
            function optRender(value, p, record) {
                return String.format('<input type="image" src="' + appPath + '/scripts/icons/page_edit.png" title="编辑" onclick="App.rankingList.edit(' + value + ')"/>');
            }
            */

            panel.add(this.grid);

        },

        /*
        del: function () {
            if (App.rankingList.grid.getSelectionModel().hasSelection()) {
                var recs = App.rankingList.grid.getSelectionModel().getSelections();
                var ids = [];
                var words = '';
                for (var i = 0; i < recs.length; i++) {
                    var data = recs[i].data;
                    ids.push(data.bookId);
                    words += data.bookName + '<br>';
                }
                Ext.Msg.confirm('删除数据', '确定删除以下榜单数据？<br><font color="red">' + words + '</font>',
                    function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                method: 'post',
                                url: appPath + '/ranking/del.do',
                                params: {
                                    listType:Ext.getCmp("rankingList.listType").getValue(),
                                    dateType:Ext.getCmp("rankingList.dateType").getValue(),
                                    ids: ids.toString()
                                },
                                success: function (resp, opts) {
                                    var result = Ext.util.JSON.decode(resp.responseText);
                                    var info = result.info;
                                    if (result.success == 'true') {
                                        Ext.Msg.alert('信息', info);
                                        App.rankingList.store.load();
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
                Ext.Msg.alert('信息', '请选择要删除的榜单数据！');
            }
        },
        */

        /*
        sendExecute: function () {
            Ext.Msg.confirm('发布榜单', '确定发布榜单数据？',
                function (btn) {
                    if (btn == 'yes') {
                        Ext.Ajax.request({
                            method: 'post',
                            url: appPath + '/ranking/execute.do',
                            success: function (resp, opts) {
                                var result = Ext.util.JSON.decode(resp.responseText);
                                var info = result.info;
                                if (result.success == 'true') {
                                    Ext.Msg.alert('信息', info);
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
        },
        */

        rankTypeRender: function (value, p, record) {
            var name = '';
            if (value == 1) {
                name = '男生原创玄幻';
            } else if (value == 2) {
                name = '男生原创都市';
            } else if (value == 3) {
                name = '女生原创古言穿越';
            } else if (value == 4) {
                name = '女生原创都市言情';
            } else if (value == 5) {
                name = '最热出版古典';
            } else if (value == 6) {
                name = '最热出版两性文学';
            }
            return String.format(name);
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };

            this.createGrid(id);

            Ext.getCmp("rankingList.dateType").setValue(1);
        },

        init: function(params) {},

        destroy: function() {}
    };
}();
