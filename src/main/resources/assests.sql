-- we don't know how to generate root <with-no-name> (class Root) :(

create table chemical
(
    chemical_Id   varchar(10)   not null
        primary key,
    acid_Or_Base  varchar(10)   null,
    concentration varchar(50)   null,
    store_Type    varchar(50)   null,
    name          varchar(100)  null,
    quantity      int default 0 null
);

create table customer
(
    customer_Id      varchar(10)  not null
        primary key,
    customer_Name    varchar(100) null,
    customer_Address varchar(200) null,
    customer_Gender  varchar(10)  null,
    customer_Dob     date         null,
    customer_Email   varchar(100) null,
    customer_Contact varchar(10)  not null,
    constraint customer_contact_unique
        unique (customer_Contact)
);

create table employee
(
    employee_Id      varchar(10)  not null
        primary key,
    employee_Name    varchar(100) null,
    employee_Address varchar(200) null,
    employee_Gender  varchar(10)  null,
    employee_Dob     date         null,
    employee_Email   varchar(100) null,
    employee_Contact varchar(15)  null
);

create table food
(
    food_Id   varchar(10)   not null
        primary key,
    name      varchar(100)  null,
    fish_Type varchar(100)  null,
    ex_Date   date          null,
    quantity  int default 0 null
);

create table payment
(
    payment_Id varchar(10)   not null
        primary key,
    method     varchar(50)   null,
    date       date          null,
    amount     varchar(1000) null
);

create table orders
(
    order_Id    varchar(10)  not null
        primary key,
    payment_Id  varchar(10)  null,
    date        date         null,
    customer_Id varchar(10)  null,
    item        varchar(100) null,
    constraint orders_ibfk_1
        foreign key (payment_Id) references payment (payment_Id),
    constraint orders_ibfk_2
        foreign key (customer_Id) references customer (customer_Id)
);

create index customer_Id
    on orders (customer_Id);

create index payment_Id
    on orders (payment_Id);

create table supplier
(
    sup_Id          varchar(20)  not null
        primary key,
    Name            varchar(20)  null,
    Contact         varchar(10)  null,
    Company_Address varchar(100) null,
    Supply_Type     varchar(30)  null,
    Email           varchar(30)  null
);

create table inventory
(
    inventory_Id varchar(10) not null
        primary key,
    sup_Id       varchar(10) null,
    date         date        null,
    constraint inventory_supplier_sup_Id_fk
        foreign key (sup_Id) references supplier (sup_Id)
);

create table chemical_detail
(
    chemical_Id  varchar(10) not null,
    quantity     varchar(10) null,
    price        varchar(10) null,
    inventory_Id varchar(10) null,
    constraint chemical_detail_ibfk_1
        foreign key (chemical_Id) references chemical (chemical_Id),
    constraint chemical_detail_ibfk_2
        foreign key (inventory_Id) references inventory (inventory_Id)
);

create index chemical_Id
    on chemical_detail (chemical_Id);

create index inventory_Id
    on chemical_detail (inventory_Id);

create table food_detail
(
    food_Id      varchar(10) null,
    quantity     varchar(10) null,
    price        varchar(10) null,
    inventory_Id varchar(10) null,
    constraint food_detail_ibfk_1
        foreign key (food_Id) references food (food_Id),
    constraint food_detail_ibfk_2
        foreign key (inventory_Id) references inventory (inventory_Id)
);

create index food_Id
    on food_detail (food_Id);

create index inventory_Id
    on food_detail (inventory_Id);

create table tank
(
    tank_Id       varchar(10) not null
        primary key,
    glass_Type    varchar(50) null,
    fish_Or_Plant varchar(50) null,
    water_Type    varchar(50) null
);

create table fish
(
    fish_Id    varchar(10)   not null
        primary key,
    name       varchar(100)  null,
    size       varchar(50)   null,
    tank_Id    varchar(10)   null,
    gender     varchar(10)   null,
    water_Type varchar(50)   null,
    country    varchar(50)   null,
    colour     varchar(50)   null,
    quantity   int default 0 null,
    constraint fish_ibfk_1
        foreign key (tank_Id) references tank (tank_Id)
);

create index tank_Id
    on fish (tank_Id);

create table fish_detail
(
    fish_Id      varchar(10) null,
    quantity     varchar(10) null,
    price        varchar(10) null,
    inventory_Id varchar(10) null,
    constraint fish_detail_ibfk_1
        foreign key (fish_Id) references fish (fish_Id),
    constraint fish_detail_ibfk_2
        foreign key (inventory_Id) references inventory (inventory_Id)
);

create index fish_Id
    on fish_detail (fish_Id);

create index inventory_Id
    on fish_detail (inventory_Id);

create table order_fish
(
    order_Id varchar(10) null,
    fish_Id  varchar(10) null,
    constraint order_fish_ibfk_1
        foreign key (order_Id) references orders (order_Id),
    constraint order_fish_ibfk_2
        foreign key (fish_Id) references fish (fish_Id)
);

create index fish_Id
    on order_fish (fish_Id);

create index order_Id
    on order_fish (order_Id);

create table ph_chemical
(
    ph_Level      varchar(10) null,
    tank_Id       varchar(10) null,
    check_In_Time time        null,
    chemical_Id   varchar(10) null,
    date          date        null,
    constraint ph_chemical_ibfk_1
        foreign key (tank_Id) references tank (tank_Id),
    constraint ph_chemical_ibfk_2
        foreign key (chemical_Id) references chemical (chemical_Id)
);

create index chemical_Id
    on ph_chemical (chemical_Id);

create index tank_Id
    on ph_chemical (tank_Id);

create table plant
(
    plant_Id   varchar(10)   not null
        primary key,
    name       varchar(100)  null,
    water_Type varchar(50)   null,
    tank_Id    varchar(10)   null,
    size       varchar(50)   null,
    quantity   int default 0 null,
    constraint plant_ibfk_2
        foreign key (tank_Id) references tank (tank_Id)
);

create table order_plant
(
    order_Id varchar(10) null,
    plant_Id varchar(10) null,
    constraint order_plant_ibfk_1
        foreign key (order_Id) references orders (order_Id),
    constraint order_plant_ibfk_2
        foreign key (plant_Id) references plant (plant_Id)
);

create index order_Id
    on order_plant (order_Id);

create index plant_Id
    on order_plant (plant_Id);

create index tank_Id
    on plant (tank_Id);

create table plant_detail
(
    plant_Id     varchar(10) null,
    quantity     varchar(10) null,
    price        varchar(10) null,
    inventory_Id varchar(10) null,
    constraint plant_detail_ibfk_1
        foreign key (plant_Id) references plant (plant_Id),
    constraint plant_detail_ibfk_2
        foreign key (inventory_Id) references inventory (inventory_Id)
);

create index inventory_Id
    on plant_detail (inventory_Id);

create index plant_Id
    on plant_detail (plant_Id);

create table ticket
(
    ticket_Id   varchar(10)                      not null
        primary key,
    age         varchar(10)                      null,
    price       varchar(10)                      null,
    date        date                             null,
    time        time                             null,
    customer_Id varchar(10) default 'NonLoyalty' null,
    employee_Id varchar(10)                      null,
    Quantity    varchar(10)                      null,
    Full_Price  varchar(10)                      not null,
    constraint ticket_ibfk_1
        foreign key (customer_Id) references customer (customer_Id),
    constraint ticket_ibfk_2
        foreign key (employee_Id) references employee (employee_Id)
);

create index customer_Id
    on ticket (customer_Id);

create index employee_Id
    on ticket (employee_Id);