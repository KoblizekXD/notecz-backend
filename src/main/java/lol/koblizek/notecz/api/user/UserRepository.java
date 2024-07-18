package lol.koblizek.notecz.api.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @NonNull User saveAndFlush(@NonNull User user);

    @Override
    <S extends User> S save(S entity);
}
