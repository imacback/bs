App.recommendLogList = function () {
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
                    url: appPath + '/recommendLog/list.do'
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
                            name: 'bookId',
                            type: 'long'
                        },
                        {
                            name: 'bookName',
                            allowBlank: false
                        },
                        {
                            name: 'providerName',
                            allowBlank: false
                        },
                        {
                            name: 'providerName',
                            allowBlank: false
                        },
                        {
                            name: 'targetName',
                            allowBlank: false
                        },
                        {
                            name: 'pageName',
                            allowBlank: false
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
                            name: "editDate",
                            type: 'date',
                            dateFormat: 'time'
                        },
                        {
                            name: 'platformName',
                            allowBlank: false
                        }
                    ]
                })
            });

            store.on('beforeload', function (thiz, options) {
                Ext.apply(
                    thiz.baseParams,
                    {
                        bookId: Ext.getCmp("recommendLogList.bookId").getValue()
                    }
                );
            });

            return store;
        },

        createGrid: function (id) {

            var panel = Ext.getCmp(id);

            panel.body.dom.innerHTML = "";

            this.grid = new Ext.grid.GridPanel({
                loadMask: true,
                tbar: [
                    '书籍ID：',
                    {
                        xtype: 'textfield',
                        id: 'recommendLogList.bookId',
                        width: 80
                    },
                    {xtype: 'tbseparator'},
                    {
                        xtype: 'button',
                        text: '查找',
                        iconCls: 'icon-search',
                        handler: function () {
                            App.recommendLogList.store.load();
                        }
                    }
                ],
                store: this.store,
                columns: [
                    {
                        header: "ID",
                        width: 60,
                        sortable: true,
                        dataIndex: 'id',
                        align: 'center',
                        hideable: false
                    },
                    {
                        header: "书籍ID",
                        width: 100,
                        sortable: true,
                        dataIndex: 'bookId',
                        align: 'center'
                    },
                    {
                        header: "书名",
                        width: 200,
                        sortable: true,
                        dataIndex: 'bookName',
                        align: 'center'
                    },
                    {
                        header: "版权",
                        width: 100,
                        sortable: true,
                        dataIndex: 'providerName',
                        align: 'center'
                    },
                    {
                        header: "推荐栏目",
                        width: 100,
                        sortable: true,
                        dataIndex: 'targetName',
                        align: 'center'
                    },
                    {
                        header: "页面",
                        width: 100,
                        sortable: true,
                        dataIndex: 'pageName',
                        align: 'center'
                    },
                    {
                        header: "开始时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'createDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },
                    {
                        header: "结束时间",
                        width: 80,
                        sortable: true,
                        dataIndex: 'editDate',
                        align: 'center',
                        renderer: Ext.util.Format.dateRenderer('Y-m-d')
                    },
                    {
                        header: "推荐人",
                        width: 100,
                        sortable: true,
                        dataIndex: 'creatorName',
                        align: 'center'
                    },
                    {
                        header: "平台",
                        width: 100,
                        sortable: true,
                        dataIndex: 'platformName',
                        align: 'center'
                    }
                ],
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

        init: function(params) {
            Ext.getCmp("recommendLogList.bookId").setValue(params.bookId);
            this.store.reload();
        },

        render: function (id) {
            if (!this.store) {
                this.store = this.getStore();
            };

            this.createGrid(id);
        },

        destroy: function() {}
    };
}();
