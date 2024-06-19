package com.github.cargoclean.core.usecase;

import com.github.cargoclean.core.AlwaysOkSecurity;
import com.github.cargoclean.core.port.ErrorHandlingPresenterOutputPort;
import com.github.cargoclean.core.port.security.SecurityOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionOperationsOutputPort;
import com.github.cargoclean.core.port.transaction.TransactionRunnableWithResult;
import com.github.cargoclean.core.port.transaction.TransactionRunnableWithoutResult;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

/*
    References:
    ----------

    1. Code adapted from https://github.com/gushakov/cleanddd/blob/main/src/test/java/com/github/cleanddd/usecase/EnrollStudentUseCaseTest.java

 */

/**
 * Bootstraps unit tests for all use cases. Creates stubs for pass-through security
 * assertions and transaction demarcation operations.
 *
 * @see #commonSetUp()
 */
public abstract class AbstractUseCaseTestSupport {


    protected SecurityOutputPort securityOps;

    @Mock
    protected TransactionOperationsOutputPort txOps;

    /**
     * Should be called from {@code setUp()} method of every concrete use case test.
     */
    @BeforeEach
    protected void commonSetUp() {

        // mock security
        securityOps = new AlwaysOkSecurity();

        // mock transaction operations
        lenient().doAnswer(invocation -> {
            ((TransactionRunnableWithoutResult) invocation.getArgument(0)).run();
            return null;
        }).when(txOps).doInTransaction(any(TransactionRunnableWithoutResult.class));

        lenient().when(txOps.doInTransactionWithResult(any(TransactionRunnableWithResult.class)))
                .thenAnswer(invocation -> ((TransactionRunnableWithResult<?>) invocation.getArgument(0)).run());

        lenient().when(txOps.doInTransactionWithResult(anyBoolean(), any(TransactionRunnableWithResult.class)))
                .thenAnswer(invocation -> ((TransactionRunnableWithResult<?>) invocation.getArgument(1)).run());

        lenient().doAnswer(invocation -> {
            ((TransactionRunnableWithoutResult) invocation.getArgument(0)).run();
            return null;
        }).when(txOps).doAfterCommit(any(TransactionRunnableWithoutResult.class));

        lenient().doAnswer(invocation -> {
            ((TransactionRunnableWithoutResult) invocation.getArgument(0)).run();
            return null;
        }).when(txOps).doAfterRollback(any(TransactionRunnableWithoutResult.class));

    }

    protected abstract ErrorHandlingPresenterOutputPort getPresenter();

    protected void noErrorsWerePresented() {
        verify(getPresenter(), times(0)).presentError(any(Exception.class));
    }
}
