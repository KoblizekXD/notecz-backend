package lol.koblizek.notecz.api.user;

import jakarta.validation.Valid;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @NonNull User saveAndFlush(@Valid @NonNull User user);
}
