create schema if not exists CurrencyExchanger;

create table if not exists CurrencyExchanger.Currencies (
    ID       serial primary key,
    Code     varchar(10) unique not null,
    FullName varchar(100) not null,
    Sign     varchar(1) not null
);

create table if not exists CurrencyExchanger.ExchangeRates (
    ID serial primary key,
    BaseCurrencyId int not null references CurrencyExchanger.currencies(ID),
    TargetCurrencyId int not null references CurrencyExchanger.currencies(ID),
    Rate double precision not null
);