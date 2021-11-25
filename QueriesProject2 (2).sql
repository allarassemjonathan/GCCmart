select * from product where name like "%ketchup%";
select avg(NProducts) from store;
select product.ProductID, store.name, store.location from product join inventory using(ProductID) join store on code = Store_ID where ProductID = 2234 or ProductID = 2233;
select couponCode, ProductID, product.name, product.brand, product.price, cost - price,  expDate from coupon join product using(ProductID) where name like "%coffee%";
select * from invoice where RewardsNo = 112 and dateOfPurchase = "2021-03-26";
select count(*) from purchase where CouponCode is not null;
select * from store where YTDsales >= all(select YTDsales from store); 
select num, code, name, NProducts from (select *, count(InvoiceID) as num from store join invoice where StoreID = code group by code) as tab where num >= all (select count(InvoiceID) as num from store join invoice where StoreID = code group by code);
select avg(InvoiceTotal), RewardsNo from invoice group by InvoiceID;
select brand from (select brand,count(InvoiceID) as numInvoice from inventory join invoice on Store_ID = StoreID join product using(ProductID) where brand is not null group by brand) as tab1 where numInvoice >= all(select count(InvoiceID) as numInvoice from inventory join invoice on Store_ID = StoreID join product using(ProductID) where brand is not null group by brand);
select InvoiceTotal, numInvoice, count(ProductID) from (select *, count(InvoiceID) as numInvoice from invoice join inventory on Store_ID = StoreID join product using(ProductID) where month(dateOfPurchase) >= all(select month(dateOfPurchase) from invoice) group by ProductID) as tab1 group by name;
select * from store;
select * from inventory;
delimiter &
#trigger 1

create trigger StoreStartSelling 
after insert on inventory
for each row begin 
update store
set Nproducts = Nproducts + 1
where new.code = Store_ID;
end&

select * from store;



select price from product join inventory using(ProductID) join invoice on  Store_ID = StoreID where new.StoreID = Store_ID;

#trigger 2
create trigger NewInvoiceGenerated
after insert on invoice
for each row begin
update store
set YTDsales = YTDsales + (select price from product join inventory using(ProductID) join invoice on  Store_ID = StoreID where new.StoreID = Store_ID)
where new.Store_ID = code;
end&