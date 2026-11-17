
    create table categoria (
       id  bigserial not null,
        nome varchar(60) not null,
        departamento_id int8 not null,
        primary key (id)
    )

    create table cliente (
       ativo boolean not null,
        data_nascimento date,
        id int8 not null,
        primary key (id)
    )

    create table departamento (
       id  bigserial not null,
        nome varchar(60) not null,
        primary key (id)
    )

    create table fornecedor (
       id  bigserial not null,
        cnpj varchar(18) not null,
        razao_social varchar(120) not null,
        primary key (id)
    )

    create table funcionario (
       cargo varchar(20) not null,
        data_admissao date,
        salario numeric(10, 2),
        id int8 not null,
        loja_id int8 not null,
        primary key (id)
    )

    create table item_pedido (
       id  bigserial not null,
        desconto numeric(10, 2) not null,
        preco_unitario numeric(10, 2) not null,
        quantidade int4 not null,
        pedido_id int8 not null,
        produto_id int8 not null,
        primary key (id)
    )

    create table loja (
       id  bigserial not null,
        bairro varchar(80),
        cep varchar(9),
        cidade varchar(80),
        estado varchar(2),
        logradouro varchar(150),
        numero varchar(10),
        nome varchar(100) not null,
        primary key (id)
    )

    create table pagamento (
       id  bigserial not null,
        data_pagamento timestamp,
        forma varchar(20) not null,
        parcelas int4 not null,
        status varchar(20) not null,
        valor numeric(12, 2) not null,
        primary key (id)
    )

    create table pedido (
       id  bigserial not null,
        data_hora timestamp not null,
        status varchar(25) not null,
        valor_frete numeric(10, 2) not null,
        cliente_id int8 not null,
        loja_id int8 not null,
        pagamento_id int8,
        primary key (id)
    )

    create table pessoa (
       id  bigserial not null,
        cpf varchar(14) not null,
        data_cadastro date not null,
        email varchar(120) not null,
        bairro varchar(80),
        cep varchar(9),
        cidade varchar(80),
        estado varchar(2),
        logradouro varchar(150),
        numero varchar(10),
        nome varchar(120) not null,
        primary key (id)
    )

    create table produto (
       id  bigserial not null,
        ativo boolean not null,
        data_cadastro date,
        descricao varchar(500),
        estoque int4 not null,
        marca varchar(60),
        nome varchar(150) not null,
        preco numeric(10, 2) not null,
        categoria_id int8 not null,
        primary key (id)
    )

    create table produto_fornecedor (
       produto_id int8 not null,
        fornecedor_id int8 not null,
        primary key (produto_id, fornecedor_id)
    )

    alter table categoria 
       add constraint UK_prx5elpv558ah8pk8x18u56yc unique (nome)

    alter table departamento 
       add constraint UK_d0gdbc1oh2ffvl6f54xvxghrr unique (nome)

    alter table fornecedor 
       add constraint UK_rihyjxxhmgutledr1hdl5gpi8 unique (cnpj)

    alter table pedido 
       add constraint UK_lo2k5meix49weja2bwx3ixa21 unique (pagamento_id)

    alter table pessoa 
       add constraint UK_nlwiu48rutiltbnjle59krljo unique (cpf)

    alter table pessoa 
       add constraint UK_mc87q8fpvldpdyfo9o5633o5l unique (email)

    alter table categoria 
       add constraint FK4p96j3lmrc5jf8fe8o1dtma9q 
       foreign key (departamento_id) 
       references departamento

    alter table cliente 
       add constraint FKa710hls1tg15o3d4ok5biathy 
       foreign key (id) 
       references pessoa

    alter table funcionario 
       add constraint FKd9ngjtrh226aoprebowwb9nxi 
       foreign key (loja_id) 
       references loja

    alter table funcionario 
       add constraint FKpjvvctvfjm69dvye3aea5k56f 
       foreign key (id) 
       references pessoa

    alter table item_pedido 
       add constraint FK60ym08cfoysa17wrn1swyiuda 
       foreign key (pedido_id) 
       references pedido

    alter table item_pedido 
       add constraint FKtk55mn6d6bvl5h0no5uagi3sf 
       foreign key (produto_id) 
       references produto

    alter table pedido 
       add constraint FK30s8j2ktpay6of18lbyqn3632 
       foreign key (cliente_id) 
       references cliente

    alter table pedido 
       add constraint FK40ut7ly3j1s4ydhfrjkt635ux 
       foreign key (loja_id) 
       references loja

    alter table pedido 
       add constraint FKd8wwc5gy5hnijj7iki22jeorn 
       foreign key (pagamento_id) 
       references pagamento

    alter table produto 
       add constraint FKopu9jggwnamfv0c8k2ri3kx0a 
       foreign key (categoria_id) 
       references categoria

    alter table produto_fornecedor 
       add constraint FKn1k3cs5wvvvwbg2xkpbwrqhnn 
       foreign key (fornecedor_id) 
       references fornecedor

    alter table produto_fornecedor 
       add constraint FKjndqo3rv2gc9bsttkvcet0y98 
       foreign key (produto_id) 
       references produto
