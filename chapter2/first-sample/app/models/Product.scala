package models

case class Product(ean: Long, name: String, description: String)        // 모델 클래스

object Product {    // 데이터 접근 객체
    var products = Set( Product(5010255079763L, "Paperclips Large",
        "Large Plain Pack of 1000"),
            Product(5018206244666L, "Giant Paperclips",
        "Giant Plain 51mm 100 pack"),
            Product(5018306332812L, "Paperclip Giant Plain",
        "Giant Plain Pack of 10000"),
            Product(5018306312913L, "No Tear Paper Clip",
        "No Tear Extra Large Pack of 1000"),
            Product(5018206244611L, "Zebra Paperclips",
        "Zebra Length 28mm Assorted 150 Pack")
        )

    def findAll = products.toList.sortBy(_.ean)     // 찾기 함수

    def findByEan(ean: Long) = products.find(_.ean == ean)

    def add(product: Product) {       // 새로운 상품 추가
      products = products + product
    }
}