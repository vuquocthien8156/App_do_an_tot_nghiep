package com.example.qthien.t__t.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailOrder(
    @SerializedName("ma_chi_tiet")
    var idDetailOrder : Int,
    @SerializedName("ma_san_pham")
    var idProduct : Int,
    @SerializedName("ten")
    var nameProduct : String,
    @SerializedName("so_luong")
    var quantity : Int,
    @SerializedName("don_gia")
    var unitPrice : Long,
    @SerializedName("kich_co")
    var size : String,
    @SerializedName("gia_khuyen_mai")
    var priceDiscount : Long,
    @SerializedName("thanh_tien")
    var total : Long,
    @SerializedName("ghi_chu")
    var note : String? = "",
    @SerializedName("topping")
    var arrTopping : ArrayList<ToppingOrder>
    ) : Parcelable


//{
//    "Detail" : [
//    {
//        "don_gia": 35000,
//        "gia_khuyen_mai": 0,
//        "kich_co": "M",
//        "ma_chi_tiet": 0,
//        "ma_san_pham": 16,
//        "so_luong": 1,
//        "ten": "Trà sữa cà phê",
//        "thanh_tien": 51000,
//        "ghi_chu": "",
//        "topping": [
//        {"ma_san_pham": 63, "ten": "Thạch trái cây", "so_luong": 1, "don_gia": 6000},
//        {"ma_san_pham": 64, "ten": "Bánh flan", "so_luong": 1, "don_gia": 10000}
//        ]
//    }
//    ],
//    "thong_tin_DH": {
//    "ghi_chu": "",
//    "khuyen_mai": 0,
//    "ma_chu": "",
//    "ma_don_hang": 0,
//    "ma_khach_hang": 70,
//    "ngay_lap": "",
//    "phi_ship": 0,
//    "phuong_thuc_thanh_toan": 1,
//    "so_diem": 0,
//    "thong_tin_giao_hang": "Quốc Thiênnn - 0782328156 - 47 Đường số 3, Bình Chiểu, Thủ Đức, Hồ Chí Minh, Việt Nam",
//    "tong_tien": 305000,
//    "tong_tien_khuyen_mai": 0,
//    "trang_thai": []
//}
//}