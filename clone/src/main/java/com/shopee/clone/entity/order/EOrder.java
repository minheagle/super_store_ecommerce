package com.shopee.clone.entity.order;


public enum EOrder {
    //    Đơn hàng chưa được xử lý hoặc xác nhận.
    Pending,
    //    Đơn hàng đã nhận được tiền và đang chờ xác nhận.
    Transferred,

//    Đơn hàng đang được xử lý và chuẩn bị để giao hàng.
    Processing,

//    Đơn hàng đã được giao cho dịch vụ vận chuyển và đang trong quá trình vận chuyển đến địa chỉ của khách hàng.
    Shipped,
//    Đơn hàng đã được giao thành công và đã đến tay khách hàng.
    Delivered,
//    Đơn hàng đã bị hủy bỏ và không được xử lý hoặc giao hàng.
    Cancelled,
//    Khách hàng đã được hoàn tiền cho đơn hàng.
    Refunded,
//    Đơn hàng đã hoàn thành, tất cả các quy trình đã được thực hiện và thanh toán đã được xác nhận.
    Completed
}
