--
-- PostgreSQL database dump
--

\restrict 7Jf8aampVcteaPW489LetCLnNNbzO1IvWuTMFdkeTSzuvZAZl7TQOBVPR8nwVHY

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.6

-- Started on 2025-09-08 21:16:32

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 22066)
-- Name: productos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.productos (
    id integer NOT NULL,
    nombre character varying(100) NOT NULL,
    descripcion text,
    precio numeric(10,2) NOT NULL,
    stock integer DEFAULT 0 NOT NULL,
    imagen_url character varying(255),
    categoria character varying(100)
);


ALTER TABLE public.productos OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 22065)
-- Name: productos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.productos_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.productos_id_seq OWNER TO postgres;

--
-- TOC entry 4911 (class 0 OID 0)
-- Dependencies: 219
-- Name: productos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.productos_id_seq OWNED BY public.productos.id;


--
-- TOC entry 218 (class 1259 OID 22055)
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuarios (
    id integer NOT NULL,
    nombre character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(255) NOT NULL,
    rol character varying(20) DEFAULT 'CLIENTE'::character varying NOT NULL
);


ALTER TABLE public.usuarios OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 22054)
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuarios_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_id_seq OWNER TO postgres;

--
-- TOC entry 4912 (class 0 OID 0)
-- Dependencies: 217
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- TOC entry 4749 (class 2604 OID 22069)
-- Name: productos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos ALTER COLUMN id SET DEFAULT nextval('public.productos_id_seq'::regclass);


--
-- TOC entry 4747 (class 2604 OID 22058)
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- TOC entry 4905 (class 0 OID 22066)
-- Dependencies: 220
-- Data for Name: productos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.productos (id, nombre, descripcion, precio, stock, imagen_url, categoria) FROM stdin;
6	yogur	aa	2.00	24	1757066896786_yogur.png	lacteos
7	er		6.00	2	\N	Abarrotes
8	manzanas	ricas y nutritivas	2.00	15	1757068003934_manzana.png	Frutas
1	leche	evaporada	5.00	14	1757380593060_leche.png	Lacteos
3	arroz	por kilo	3.00	56	1757380630921_arroz.png	Abarrotes
2	azucar		3.00	12	1757380746257_Azucar.png	Abarrotes
\.


--
-- TOC entry 4903 (class 0 OID 22055)
-- Dependencies: 218
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.usuarios (id, nombre, email, password, rol) FROM stdin;
1	Trabajador Uno	trabajador@mitienda.com	trabajador123	TRABAJADOR
2	Admin General	admin@mitienda.com	admin123	ADMIN
3	Angello	usuario@gmail.com	usuario003	CLIENTE
\.


--
-- TOC entry 4913 (class 0 OID 0)
-- Dependencies: 219
-- Name: productos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.productos_id_seq', 8, true);


--
-- TOC entry 4914 (class 0 OID 0)
-- Dependencies: 217
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 4, true);


--
-- TOC entry 4756 (class 2606 OID 22074)
-- Name: productos productos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.productos
    ADD CONSTRAINT productos_pkey PRIMARY KEY (id);


--
-- TOC entry 4752 (class 2606 OID 22063)
-- Name: usuarios usuarios_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_email_key UNIQUE (email);


--
-- TOC entry 4754 (class 2606 OID 22061)
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


-- Completed on 2025-09-08 21:16:33

--
-- PostgreSQL database dump complete
--

\unrestrict 7Jf8aampVcteaPW489LetCLnNNbzO1IvWuTMFdkeTSzuvZAZl7TQOBVPR8nwVHY

