insert into user_tbl (username, password, activated) values ('admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', 1);
insert into user_tbl (username, password, activated) values ('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 1);

insert into authority_tbl (authority_name) values ('ROLE_USER');
insert into authority_tbl (authority_name) values ('ROLE_ADMIN');

insert into user_authority (user_idx, authority_name) values (1, 'ROLE_USER');
insert into user_authority (user_idx, authority_name) values (1, 'ROLE_ADMIN');
insert into user_authority (user_idx, authority_name) values (2, 'ROLE_USER');