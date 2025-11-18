CREATE TABLE IF NOT EXISTS usuario(
	id_usuario SERIAL NOT NULL,
	nome VARCHAR(50) NOT NULL,
	senha VARCHAR(50) NOT NULL,
	descricao VARCHAR(100),
	email VARCHAR(100) UNIQUE NOT NULL,
	
	PRIMARY KEY (id_usuario)
);

CREATE TABLE IF NOT EXISTS post(
	id_post SERIAL NOT NULL,
	id_usuario INT NOT NULL,
	data_hora TIMESTAMP NOT NULL,
	legenda VARCHAR(200),

	PRIMARY KEY (id_post),

	FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS midia(
	id_midia SERIAL NOT NULL,
	tamanho INT NOT NULL,
	tipo BOOLEAN NOT NULL,
	duracao INT,

	PRIMARY KEY (id_midia)
);

CREATE TABLE IF NOT EXISTS mensagem(
	id_mensagem SERIAL NOT NULL,
	data_hora TIMESTAMP NOT NULL,
	texto VARCHAR(1000),
	id_usuario INT,
	id_post INT,
	id_midia INT,
	entregue BOOLEAN NOT NULL,
	visualizado BOOLEAN NOT NULL,

	PRIMARY KEY (id_mensagem),

	FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
	FOREIGN KEY (id_post) REFERENCES post(id_post),
	FOREIGN KEY (id_midia) REFERENCES midia(id_midia)
);

CREATE TABLE IF NOT EXISTS conversa(
	id_conversa SERIAL NOT NULL,
	nome_conversa VARCHAR(50) NOT NULL,

	PRIMARY KEY (id_conversa)
);

CREATE TABLE IF NOT EXISTS segue(
	id_seguidor INT NOT NULL,
	id_seguido INT NOT NULL,

	PRIMARY KEY (id_seguidor, id_seguido),

	FOREIGN KEY (id_seguidor) REFERENCES usuario(id_usuario),
	FOREIGN KEY (id_seguido) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS recebe(
	id_conversa INT NOT NULL,
	id_mensagem INT NOT NULL,

	PRIMARY KEY (id_conversa, id_mensagem),

	FOREIGN KEY (id_conversa) REFERENCES conversa(id_conversa),
	FOREIGN KEY (id_mensagem) REFERENCES mensagem(id_mensagem)
);

CREATE TABLE IF NOT EXISTS curte(
	id_post INT NOT NULL,
	id_usuario INT NOT NULL,

	PRIMARY KEY (id_post, id_usuario),

	FOREIGN KEY (id_post) REFERENCES post(id_post),
	FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS participa(
	id_conversa INT NOT NULL,
	id_usuario INT NOT NULL, 

	PRIMARY KEY (id_conversa, id_usuario),

	FOREIGN KEY (id_conversa) REFERENCES conversa(id_conversa),
	FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS comenta(
	id_post INT NOT NULL,
	id_usuario INT NOT NULL,
	texto VARCHAR(100) NOT NULL,
	dataHora TIMESTAMP NOT NULL,
	id_comentario SERIAL NOT NULL,

	PRIMARY KEY (id_comentario),

	FOREIGN KEY (id_post) REFERENCES post(id_post),
	FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE IF NOT EXISTS possui(
	id_post INT NOT NULL,
	id_midia INT NOT NULL,

	PRIMARY KEY (id_post, id_midia),

	FOREIGN KEY (id_post) REFERENCES post(id_post),
	FOREIGN KEY (id_midia) REFERENCES midia(id_midia)
);

-- DROP TABLE usuario;
-- DROP TABLE post;
-- DROP TABLE midia;
-- DROP TABLE mensagem;
-- DROP TABLE conversa;
-- DROP TABLE segue;
-- DROP TABLE recebe;
-- DROP TABLE curte;
-- DROP TABLE participa;
-- DROP TABLE comenta;
-- DROP TABLE possui;

-- SELECT * FROM usuario;
-- SELECT * FROM post;
-- SELECT * FROM midia;
-- SELECT * FROM mensagem;
-- SELECT * FROM conversa;
-- SELECT * FROM segue;
-- SELECT * FROM recebe;
-- SELECT * FROM curte;
-- SELECT * FROM participa;
-- SELECT * FROM comenta;
-- SELECT * FROM possui;