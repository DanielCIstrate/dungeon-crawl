DROP TABLE IF EXISTS public.game_state CASCADE;
CREATE TABLE public.game_state (
    id serial NOT NULL PRIMARY KEY,
    current_map text NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL
);

DROP TABLE IF EXISTS public.player CASCADE;
CREATE TABLE public.player (
    id serial NOT NULL PRIMARY KEY,
    player_name text NOT NULL,
    hp integer NOT NULL,
    x integer NOT NULL,
    y integer NOT NULL
);

DROP TABLE IF EXISTS public.gamestate_actor;
CREATE TABLE public.gamestate_actor (
      gamestate_id integer NOT NULL,
      actor_id integer NOT NULL
);

DROP TABLE IF EXISTS public.gamestate_item;
CREATE TABLE public.gamestate_item (
                    gamestate_id integer NOT NULL,
                    item_id integer NOT NULL
);

DROP TABLE IF EXISTS public.actor CASCADE;
CREATE TABLE public.actor (
      id serial NOT NULL PRIMARY KEY,
      class_name text NOT NULL,
      hp integer NOT NULL,
      x integer NOT NULL,
      y integer NOT NULL,
      default_id integer NOT NULL
);

DROP TABLE IF EXISTS public.default_actor;
CREATE TABLE public.default_actor (
      id serial NOT NULL PRIMARY KEY,
      class_name text NOT NULL,
      hp integer NOT NULL,
      x integer NOT NULL,
      y integer NOT NULL
);

DROP TABLE IF EXISTS public.item CASCADE;
CREATE TABLE public.item (
      id serial NOT NULL PRIMARY KEY,
      class_name text NOT NULL,
      is_in_inventory boolean NOT NULL,
      x integer,
      y integer,
      default_id integer NOT NULL
);

DROP TABLE IF EXISTS public.default_item;
CREATE TABLE public.default_item  (
    id serial NOT NULL PRIMARY KEY,
    class_name text NOT NULL,
    is_in_inventory boolean NOT NULL,
    x integer,
    y integer
);

ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);

ALTER TABLE ONLY public.gamestate_actor
    ADD CONSTRAINT pk_gamestate_actor_id PRIMARY KEY (gamestate_id, actor_id);

ALTER TABLE ONLY public.gamestate_actor
    ADD CONSTRAINT fk_gamestate_id FOREIGN KEY (gamestate_id) REFERENCES public.game_state(id);

ALTER TABLE ONLY public.gamestate_actor
    ADD CONSTRAINT fk_actor_id FOREIGN KEY (actor_id) REFERENCES public.actor(id);

ALTER TABLE ONLY public.gamestate_item
    ADD CONSTRAINT pk_gamestate_item_id PRIMARY KEY (gamestate_id, item_id);

ALTER TABLE ONLY public.gamestate_item
    ADD CONSTRAINT fk_gamestate_id FOREIGN KEY (gamestate_id) REFERENCES public.game_state(id);

ALTER TABLE ONLY public.gamestate_item
    ADD CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES public.item(id);

ALTER TABLE ONLY public.actor
    ADD CONSTRAINT fk_default_id FOREIGN KEY (default_id) REFERENCES public.default_actor(id);

ALTER TABLE ONLY public.item
    ADD CONSTRAINT fk_default_id FOREIGN KEY (default_id) REFERENCES public.default_item(id);