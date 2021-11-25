drop table if exists Inventory;
drop table if exists Purchase;
drop table if exists Coupon;
drop table if exists Invoice;
drop table if exists Store;
drop table if exists Product;
drop table if exists Member;

create table if not exists Member (
  RewardsNo int not null,
  Fname varchar(45) null,
  Lname varchar(45) null,
  email varchar(45) null,
  address varchar(200) null,
  primary key (RewardsNo));

create table if not exists Product (
  ProductID int not null,
  department varchar(45) null,
  name varchar(45) null,
  brand varchar(45) null,
  price double null,
  cost double null,
  quantity int null,
  primary key (ProductID));

create table if not exists Store (
  code int not null,
  name varchar(45) null,
  YTDsales double null,
  NProducts int null,
  location varchar(45) null,
  phone varchar(14) null,
  primary key (code));

create table if not exists Coupon (
  CouponCode int not null,
  ProductID int not null,
  discount double null,
  expDate date null,
  primary key (CouponCode),
  constraint fk_Coupon_Product foreign key (ProductID) references Product (ProductID)
    on delete cascade on update cascade);

create table if not exists Invoice (
  InvoiceID int not null,
  InvoiceTotal double null,
  NoItems int null,
  dateOfPurchase date null,
  timeOfPurchase time null,
  RewardsNo int not null,
  StoreID int not null,
  primary key (InvoiceID),
  constraint fk_Invoice_Store foreign key (StoreID) references Store (code) 
    on delete cascade on update cascade,
  constraint fk_Invoice_Member foreign key (RewardsNo) references Member (RewardsNo)
    on delete cascade on update cascade);

create table if not exists Purchase (
  InvoiceNo int not null,
  ProductID int not null,
  CouponCode int,
  Quantity int null,
  subtotal double null,
  primary key (InvoiceNo, ProductID),
  constraint fk_Purchase_Invoice foreign key (InvoiceNo) references Invoice (InvoiceID)
    on delete cascade on update cascade,
  constraint fk_Purchase_Product foreign key (ProductID) references Product (ProductID)
    on delete cascade on update cascade,
  constraint fk_Purchase_Coupon foreign key (CouponCode) references Coupon (CouponCode)
    on delete cascade on update cascade);

create table if not exists Inventory (
  Store_ID int not null,
  ProductID int not null,
  QtyInStore int null,
  primary key (ProductID, Store_ID),
  constraint fk_Inventory_Product foreign key (ProductID) references Product (ProductID)
    on delete cascade on update cascade,
  constraint fk_Inventory_Store foreign key (Store_ID) references Store (code)
    on delete cascade on update cascade
);
