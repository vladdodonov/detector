create index stat$event_time on statistics using brin(event_time);
create index stat$event_time_last_price on statistics using brin (event_time, last_price);
create index aggtrade$trade_time on agg_trade using brin(trade_time);
create index aggtradeprocess$symbol_event_time on agg_trade_process using brin (symbol, event_time);