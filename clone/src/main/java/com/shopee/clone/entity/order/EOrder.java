package com.shopee.clone.entity.order;


public enum EOrder {
    //    Đơn hàng chưa được xử lý hoặc xác nhận.
    Pending,
    //    Đơn hàng đã nhận được tiền và đang chờ xác nhận.
    Transferred,

//    Đang chờ thanh toán
    Awaiting_Payment,

//    Đơn hàng đang được xử lý và chuẩn bị để giao hàng.
    Processing,
//    Đơn hàng đã được giao
    Shipped,

//    Đơn hàng đã bị shop từ chối
    Rejection,
//    Đơn hàng đã bị hủy bỏ và không được xử lý hoặc giao hàng.
    Cancelled,
//    Khách hàng khong nhan duoc hang
    Fail,
//    Đơn hàng đã hoàn thành, tất cả các quy trình đã được thực hiện và thanh toán đã được xác nhận.
    Completed
}
