package sphere

import IntegrationTest.Implicits._
import io.sphere.client.shop.model._
import io.sphere.client.shop.model.PaymentState._
import io.sphere.client.shop.model.ShipmentState._
import scala.collection.JavaConversions._
import sphere.Fixtures._
import org.scalatest._
import io.sphere.client.shop.SphereClient
import com.google.common.collect.Lists


class OrderIntegrationSpec extends WordSpec with MustMatchers {
  implicit lazy val client: SphereClient = IntegrationTestClient()

  "sphere client" must {
    "change the payment state of an order" in {
      val order = newOrderOf1Product
      order.getPaymentState must be(null)
      val updatedOrder = client.orders.updateOrder(order.getIdAndVersion, new OrderUpdate().setPaymentState(CreditOwed)).execute()
      updatedOrder.getPaymentState must be(CreditOwed)
    }

    "change the shipment state of an order" in {
      val order = newOrderOf1Product
      order.getShipmentState must be (null)
      val updatedOrder = client.orders.updateOrder(order.getIdAndVersion, new OrderUpdate().setShipmentState(Shipped)).execute()
      updatedOrder.getShipmentState must be (Shipped)
    }

    "add deliveries for line items" in {
      val order = newOrderWithShippingMethod
      order.getShippingInfo must not be (null)
      order.getShippingInfo.getDeliveries must have size(0)
      val lineItem = order.getLineItems()(0)
      val items = Lists.newArrayList(new DeliveryItem(lineItem))
      val updatedOrder = client.orders.updateOrder(order.getIdAndVersion, new OrderUpdate().addDelivery(items)).execute()
      updatedOrder.getShippingInfo.getDeliveries must have size(1)
      val delivery = updatedOrder.getShippingInfo.getDeliveries.get(0)
      delivery.getItems must be(items)
    }

    "add deliveries for custom line items" in pending
    "handle error no shipping method set" in pending
  }
}
