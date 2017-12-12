$(document).ready(function () {
    $("#goods_type_select_dialog").modal('show');
    getGoodsTypeTypeOptions();
    $('#goods_type_select_type').bind('change',function(e) {
        getTreeList();
    });
    $("#goods_type_select_btn_confirm").click(function () {
        var tree = $.fn.zTree.getZTreeObj("goods_type_select_tree");
        var selectNode = tree.getSelectedNodes();
        if (!selectNode || selectNode.length == 0) {
            showTip("请选择一个分类.");
            return;
        }
        var goodsTypeNameCn = selectNode[0].goodsTypeNameCn;
        var goodsTypeId = selectNode[0].goodsTypeId;
        // 在弹出本画面的画面实现此方法
        onGoodsTypeSelected(goodsTypeId, goodsTypeNameCn);
        $("#goods_type_select_dialog").modal('hide');

    });
});

function getGoodsTypeTypeOptions() {
    var url = "/common/getCodeList.json";
    var param = {"data": "GDS_TYPE"};
    var success = function (data) {
        if (data.resultCode == '00') {
            var array = data.data;
            if (array != null) {
                $.each(array, function (index, item) {
                    var $option = $('<option>').attr('value', item.value).text(item.displayCn);
                    $("#goods_type_select_type").append($option);
                });
                getTreeList();
            }
        } else {
            showAlert('码表数据获取失败');
        }
    };
    var error = function () {
        showAlert('码表数据获取失败');
    };
    //品牌类型码表中的数据code为GDS_BRAND_TYPE 每一个码表的code不一样
    axse(url, param, success, error);
}

function getTreeList() {
    var url = '/gds_type/getTreeList.json';
    var params = {
        'type':$('#goods_type_select_type').val()
    };
    var success = function (data) {
        //返回正确的时候的业务处理
        if (data.resultCode == '00') {
            initTree(data.data);
        } else {
            //返回失败时候的业务处理
            showAlert('商品分类列表获取失败');
        }
    };
    var error = function () {
        //服务器异常处理
        showAlert('商品分类列表获取失败');
    };
    axse(url, params, success, error);
}

function initTree(data) {
    if ($.isArray(data)) {
        //参数设定
        var settings = {
            view: {
                //只能单选
                selectedMulti: false
            },
            data: {
                //画面显示项目
                key: {
                    name: "goodsTypeNameCn"
                },
                simpleData: {
                    enable: true,
                    idKey: "goodsTypeId",
                    pIdKey: "goodsTypeIdParent",
                    name: "goodsTypeNameCn",
                    rootPId: 0
                }
            },
            callback: {
                onClick: function (event, treeId, treeNode) {
                }
            }
        };
        //数据初始化
        $.fn.zTree.init($("#goods_type_select_tree"), settings, data);
    }
}

function showTip(message) {
    $("#goods_type_select_tip").text(message);
    setTimeout(function () {
        $("#goods_type_select_tip").text("");
    }, 2000);
}