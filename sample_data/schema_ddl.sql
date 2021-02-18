DROP TABLE IF EXISTS public.game_state;
CREATE TABLE public.game_state (
    id serial NOT NULL PRIMARY KEY,
    current_map text NOT NULL,
    saved_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL
);

DROP TABLE IF EXISTS public.player;
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

DROP TABLE IF EXISTS public.actor
CREATE TABLE public.actor (
      id serial NOT NULL PRIMARY KEY,
      class_name text NOT NULL,
      hp integer NOT NULL,
      x integer NOT NULL,
      y integer NOT NULL,
      default_id integer NOT NULL
);

DROP TABLE IF EXISTS public.default_actor
CREATE TABLE public.default_actor (
      id serial NOT NULL PRIMARY KEY,
      class_name text NOT NULL,
      hp integer NOT NULL,
      x integer NOT NULL,
      y integer NOT NULL
);

DROP TABLE IF EXISTS public.item
CREATE TABLE public.item (
                              id serial NOT NULL PRIMARY KEY,
                              class_name text NOT NULL,
                              is_in_inventory boolean NOT NULL,
                              x integer,
                              y integer,
                              default_id integer NOT NULL
);




ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);