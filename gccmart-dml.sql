insert into Member values (110, "William", "Peters", "wpeters@gcccomp.com", "23 Walnut Street, Pittsburgh, PA 15232"); 
insert into Member values (111, "Jill", "Johnson", "jjo@gcchello.com", "23 Walnut Street, Pittsburgh, PA 15232" );
insert into Member values (112, "Mary", "Williams", "maryw@gcccomp.com","3456 Fifth Ave, New York, NY 10012");
insert into Member values (113, "David", "Wilson", "DeeMan@gcccor.com","1152 Bell Mountain Dr., Austin, TX 78730");

insert into Product values ( 1233, "Produce", "Gala Apple 1", null, 0.99, 0.7, 2000); 
insert into Product values ( 1234, "Produce", "Banana 1 lb", null, 0.39, 0.2, 1000);
insert into Product values ( 1235, "Produce", "Tomato 1 lb", null, 2.99, 2.00, 500);
insert into Product values ( 1236, "Produce", "Potato 1 lb", null, 0.99, 0.5, 500);
insert into Product values ( 1237, "Produce", "Carrot 2 lb", null, 2.89, 2.00, 500);
insert into Product values ( 2233, "Grocery Aisle", "1 lb Coffee", "Dunkin Donuts", 7.99, 4.99, 200);
insert into Product values ( 2234, "Grocery Aisle", "1 lb Coffee", "Starbucks", 9.99, 8.99, 100);
insert into Product values ( 2235, "Grocery Aisle", "Mayonnaise 30fl oz", "French\'s", 3.49, 3.00, 200);
insert into Product values ( 2236, "Grocery Aisle", "Mayonnaise 30fl oz", "Heinz", 2.99, 2.00, 200);
insert into Product values ( 2237, "Grocery Aisle", "20 oz Ketchup", "Heinz", 2.29, 2.00, 200);
insert into Product values ( 2238, "Grocery Aisle", "28 oz Ketchup", "French\'s", 2.06, 1.50, 100);
insert into Product values ( 2239, "Grocery Aisle", "20 oz Yellow Mustard", "Heinz", 2.10, 1.60, 200);
insert into Product values ( 2240, "Grocery Aisle", "12 oz Yellow Mustard", "French\'s", 2.50, 2.00, 200);

insert into Store values( 55, "GCCMart-Waterfront", 100000, 200, "1652 main street, Pittsburgh PA, 15120", "412-111-2222");
insert into Store values( 44, "GCCMart-Cranberry", 120000, 250, "1000 main street, Cranberry Twp PA, 16066", "724-111-2222");
insert into Store values( 33, "GCCMart-Grove City", 80000, 180, "152 main street, Grove City PA, 16127", "724-111-2223");

insert into coupon values (234, 2237, 0.25, "2021-03-30");
insert into coupon values (135, 2237, 0.30, "2021-03-30");
insert into coupon values (236, 2233, 0.25, "2021-04-30");
insert into coupon values (137, 2234, 0.30, "2021-03-30");

insert into inventory values(55,1233,1000);
insert into inventory values(55,1234,500);
insert into inventory values(55,2233,100);
insert into inventory values(55,2234,100);
insert into inventory values(55,2235,100);
insert into inventory values(55,2236,100);
insert into inventory values(55,2237,100);
insert into inventory values(55,2238,80);
insert into inventory values(55,2239,100);
insert into inventory values(55,2240,80);
insert into inventory values(44,1233,500);
insert into inventory values(44,1234,250);
insert into inventory values(44,2233,50);
insert into inventory values(44,2235,50);
insert into inventory values(44,2236,50);
insert into inventory values(44,2237,50);
insert into inventory values(44,2238,20);
insert into inventory values(44,2239,50);
insert into inventory values(44,2240,50);
insert into inventory values(33,1233,500);
insert into inventory values(33,1234,250);
insert into inventory values(33,2233,50);
insert into inventory values(33,2235,50);
insert into inventory values(33,2236,50);
insert into inventory values(33,2237,50);
insert into inventory values(33,2239,50);
insert into inventory values(33,2240,50);

insert into invoice values (33122, 10.56,4, "2021-03-13", "11:05:00",110, 33);
insert into purchase values (33122, 2236, null, 2, 5.98);
insert into purchase values (33122, 2237, null, 2, 4.58);

insert into invoice values (55123, 20.15,8, "2021-03-16", "1:05:00",111, 55);
insert into purchase values (55123, 1233, null, 4, 3.96);
insert into purchase values (55123, 1235, null, 1, 2.99);
insert into purchase values (55123, 2234, null, 1, 9.99);
insert into purchase values (55123, 2237, 135, 2, 3.21);

insert into invoice values (44124, 24.03, 10, "2021-03-26", "5:05:00",112, 44);
insert into purchase values (44124, 1233, null, 4, 3.96);
insert into purchase values (44124, 1235, null, 1, 2.99);
insert into purchase values (44124, 2234, null, 1, 9.99);
insert into purchase values (44124, 1236, null, 1, 0.99);
insert into purchase values (44124, 1237, null, 1, 2.89);
insert into purchase values (44124, 2237, 135, 2, 3.21);

insert into invoice values (55124, 10.49, 2, "2021-03-25", "5:10:00",113, 55);
insert into purchase values (55124, 2233, null, 1, 7.99);
insert into purchase values (55124, 2240, null, 1, 2.50);

insert into invoice values (55125, 25.41, 19, "2021-04-01", "5:10:00",113, 55);
insert into purchase values (55125, 1233, null, 10, 9.90);
insert into purchase values (55125, 1234, null, 2, 0.78);
insert into purchase values (55125, 1235, null, 2, 5.98);
insert into purchase values (55125, 1236, null, 3, 2.97);
insert into purchase values (55125, 1237, null, 2, 5.78);

insert into invoice values (44125, 15.64, 4, "2021-04-01", "3:05:00",112, 44);
insert into purchase values (44125, 2233, null, 1, 7.99);
insert into purchase values (44125, 2235, null, 1, 3.49);
insert into purchase values (44125, 2238, null, 1, 2.06);
insert into purchase values (44125, 2239, null, 1, 2.10);
