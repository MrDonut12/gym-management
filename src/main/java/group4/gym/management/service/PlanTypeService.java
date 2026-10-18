package group4.gym.management.service;

import group4.gym.management.dao.PlanTypeDAO;
import group4.gym.management.model.Account;
import group4.gym.management.model.PlanType;
import java.util.List;

/**
 * Plan type management (CN-09). Owner creates; Owner/Manager may only edit name/price.
 */
public class PlanTypeService {

    private final PlanTypeDAO planTypeDAO = new PlanTypeDAO();

    /**
     * List all plan types.
     */
    public List<PlanType> listAll() {
        // TODO: planTypeDAO.findAll.
        // Output: list of PlanType.
        return null;
    }

    /**
     * Get a plan type by ID.
     */
    public PlanType getById(int planTypeId) {
        // TODO: planTypeDAO.findById; not found -> BusinessRuleException.
        // Output: PlanType.
        return null;
    }

    /**
     * Create a plan type within the catalog 1 day / 1 month / 6 months / 1 year (Owner only).
     */
    public int createPlanType(Account actor, PlanType planType) {
        // TODO: Check the CREATE_PLAN_TYPE permission; ValidationUtil.validatePlanType;
        //       check duplicate name after normalization; insert.
        // Output: ID of the new plan type.
        return 0;
    }

    /**
     * Edit name and price (Owner/Manager). Duration is not editable; old snapshots stay unchanged (CN-25).
     */
    public void updateNameAndPrice(Account actor, int planTypeId, String name, Long price) {
        // TODO: Check the UPDATE_PLAN_TYPE permission; validate name/price; check duplicate name (excludeId); updateNameAndPrice.
        // Output: nothing returned; throws an exception on failure.
    }
}
