package com.zao.backend.api.repository;

import com.zao.backend.tareas.ItemChecklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Acceso a datos de los items de un checklist. */
public interface ItemChecklistRepository extends JpaRepository<ItemChecklist, Long> {

    /** Lista los items activos de un checklist. */
    List<ItemChecklist> findByChecklist_IdAndActivoTrue(Long checklistId);

    /** Busca un item activo que pertenezca a un checklist. */
    Optional<ItemChecklist> findByIdAndChecklist_IdAndActivoTrue(Long id, Long checklistId);
}
