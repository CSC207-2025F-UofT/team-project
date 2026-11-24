begin;

create table public.profiles (
    id uuid primary key references auth.users (id) on delete cascade,
    username text collate pg_catalog."C" not null,
    display_name text not null,
    constraint profiles_username_unique unique (username),
    constraint profiles_username_canonical check (username operator(pg_catalog.~) '^[a-z0-9_]{3,24}$'),
    -- Unicode White_Space plus the additional Java whitespace characters used by registration.
    constraint profiles_display_name_nonblank check (pg_catalog.btrim(display_name,
        U&'\0009\000A\000B\000C\000D\001C\001D\001E\001F\0020\0085\00A0\1680\2000\2001\2002\2003\2004\2005\2006\2007\2008\2009\200A\2028\2029\202F\205F\3000') <> '')
);

alter table public.profiles enable row level security;
revoke all on table public.profiles from public, anon, authenticated, service_role;
grant select on table public.profiles to authenticated;

create policy profiles_read_own on public.profiles
    for select to authenticated
    using (id = (select auth.uid()));

create function public.insert_signup_profile()
returns trigger
language plpgsql
security definer
set search_path = ''
as $$
begin
    if pg_catalog.jsonb_typeof(new.raw_user_meta_data operator(pg_catalog.->) 'username')
            is distinct from 'string'
        or pg_catalog.jsonb_typeof(new.raw_user_meta_data operator(pg_catalog.->) 'display_name')
            is distinct from 'string' then
        raise exception using errcode = '23514', message = 'Required profile metadata is invalid.';
    end if;
    insert into public.profiles (id, username, display_name) values (
        new.id,
        new.raw_user_meta_data operator(pg_catalog.->>) 'username',
        new.raw_user_meta_data operator(pg_catalog.->>) 'display_name'
    );
    return new;
end;
$$;

revoke all on function public.insert_signup_profile() from public, anon, authenticated, service_role;

create trigger create_signup_profile
    after insert on auth.users
    for each row execute function public.insert_signup_profile();

create function public.username_is_available(candidate text)
returns boolean
language sql
stable
security definer
set search_path = ''
as $$
    select case
        when candidate collate pg_catalog."C" operator(pg_catalog.~) '^[a-z0-9_]{3,24}$'
        then not exists (select 1 from public.profiles where username operator(pg_catalog.=) candidate)
        else false
    end;
$$;

revoke all on function public.username_is_available(text) from public, anon, authenticated, service_role;
grant execute on function public.username_is_available(text) to anon;

commit;
