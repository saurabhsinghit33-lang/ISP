# 1. Create directory structure
$base = "src\main\java\com\example\isp"
New-Item -ItemType Directory -Force -Path "$base\contract", "$base\dto", "$base\service", "$base\controller" | Out-Null

# 2. pom.xml
@'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.4</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>isp</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>isp</name>
    <description>Enterprise BFSI Demo for Interface Segregation Principle</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <jvmArguments>--enable-native-access=ALL-UNNAMED</jvmArguments>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
'@ | Set-Content -Path "pom.xml" -Encoding UTF8

# 3. Main Application Class
@'
package com.example.isp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IspApplication {
    public static void main(String[] args) {
        SpringApplication.run(IspApplication.class, args);
    }
}
'@ | Set-Content -Path "$base\IspApplication.java" -Encoding UTF8

# 4. Contracts (Interfaces)
@'
package com.example.isp.contract;

import com.example.isp.dto.CashWithdrawalRequest;
import com.example.isp.dto.TransactionReceipt;

public interface CashWithdrawable {
    TransactionReceipt withdrawCash(CashWithdrawalRequest request);
}
'@ | Set-Content -Path "$base\contract\CashWithdrawable.java" -Encoding UTF8

@'
package com.example.isp.contract;

import com.example.isp.dto.FundTransferRequest;
import com.example.isp.dto.TransactionReceipt;

public interface DigitalFundTransferable {
    TransactionReceipt transferNeft(FundTransferRequest request);
    TransactionReceipt transferImps(FundTransferRequest request);
}
'@ | Set-Content -Path "$base\contract\DigitalFundTransferable.java" -Encoding UTF8

@'
package com.example.isp.contract;

import com.example.isp.dto.CreditAdjustmentRequest;
import com.example.isp.dto.TransactionReceipt;

public interface CreditFacilityManageable {
    TransactionReceipt adjustOverdraftLimit(CreditAdjustmentRequest request);
}
'@ | Set-Content -Path "$base\contract\CreditFacilityManageable.java" -Encoding UTF8

# 5. DTO Records
@'
package com.example.isp.dto;

import java.math.BigDecimal;

public record CashWithdrawalRequest(
        String accountId,
        String atmTerminalId,
        BigDecimal amount
) {}
'@ | Set-Content -Path "$base\dto\CashWithdrawalRequest.java" -Encoding UTF8

@'
package com.example.isp.dto;

import java.math.BigDecimal;

public record FundTransferRequest(
        String sourceAccountId,
        String targetAccountId,
        String beneficiaryIfsc,
        BigDecimal amount
) {}
'@ | Set-Content -Path "$base\dto\FundTransferRequest.java" -Encoding UTF8

@'
package com.example.isp.dto;

import java.math.BigDecimal;

public record CreditAdjustmentRequest(
        String accountId,
        String officerEmployeeId,
        BigDecimal newOverdraftLimit
) {}
'@ | Set-Content -Path "$base\dto\CreditAdjustmentRequest.java" -Encoding UTF8

@'
package com.example.isp.dto;

import java.time.LocalDateTime;

public record TransactionReceipt(
        String referenceNumber,
        String accountId,
        String status,
        String remarks,
        LocalDateTime timestamp
) {}
'@ | Set-Content -Path "$base\dto\TransactionReceipt.java" -Encoding UTF8

# 6. Service Implementations
@'
package com.example.isp.service;

import com.example.isp.contract.CashWithdrawable;
import com.example.isp.dto.CashWithdrawalRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AtmBankingService implements CashWithdrawable {

    @Override
    public TransactionReceipt withdrawCash(CashWithdrawalRequest request) {
        System.out.println("Dispensing physical cash of $" + request.amount() + " from ATM: " + request.atmTerminalId());
        return new TransactionReceipt(
                "ATM-" + UUID.randomUUID().toString().substring(0, 8),
                request.accountId(),
                "COMPLETED",
                "Cash dispensed successfully",
                LocalDateTime.now()
        );
    }
}
'@ | Set-Content -Path "$base\service\AtmBankingService.java" -Encoding UTF8

@'
package com.example.isp.service;

import com.example.isp.contract.DigitalFundTransferable;
import com.example.isp.dto.FundTransferRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DigitalBankingService implements DigitalFundTransferable {

    @Override
    public TransactionReceipt transferNeft(FundTransferRequest request) {
        System.out.println("Queued NEFT batch payment: $" + request.amount() + " to " + request.targetAccountId());
        return buildReceipt(request.sourceAccountId(), "NEFT transfer queued");
    }

    @Override
    public TransactionReceipt transferImps(FundTransferRequest request) {
        System.out.println("Executed real-time IMPS settlement: $" + request.amount() + " to " + request.targetAccountId());
        return buildReceipt(request.sourceAccountId(), "IMPS transfer completed instantly");
    }

    private TransactionReceipt buildReceipt(String accountId, String remarks) {
        return new TransactionReceipt(
                "DIGI-" + UUID.randomUUID().toString().substring(0, 8),
                accountId,
                "COMPLETED",
                remarks,
                LocalDateTime.now()
        );
    }
}
'@ | Set-Content -Path "$base\service\DigitalBankingService.java" -Encoding UTF8

@'
package com.example.isp.service;

import com.example.isp.contract.CreditFacilityManageable;
import com.example.isp.dto.CreditAdjustmentRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BranchOperationsService implements CreditFacilityManageable {

    @Override
    public TransactionReceipt adjustOverdraftLimit(CreditAdjustmentRequest request) {
        System.out.println("Branch Officer " + request.officerEmployeeId() + 
                " authorized new OD limit of $" + request.newOverdraftLimit() + 
                " for account " + request.accountId());

        return new TransactionReceipt(
                "OD-" + UUID.randomUUID().toString().substring(0, 8),
                request.accountId(),
                "APPROVED",
                "Overdraft limit updated",
                LocalDateTime.now()
        );
    }
}
'@ | Set-Content -Path "$base\service\BranchOperationsService.java" -Encoding UTF8

# 7. Controllers
@'
package com.example.isp.controller;

import com.example.isp.contract.CashWithdrawable;
import com.example.isp.dto.CashWithdrawalRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/atm")
public class AtmController {

    private final CashWithdrawable cashService;

    public AtmController(CashWithdrawable cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionReceipt> withdraw(@RequestBody CashWithdrawalRequest request) {
        return ResponseEntity.ok(cashService.withdrawCash(request));
    }
}
'@ | Set-Content -Path "$base\controller\AtmController.java" -Encoding UTF8

@'
package com.example.isp.controller;

import com.example.isp.contract.DigitalFundTransferable;
import com.example.isp.dto.FundTransferRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/digital")
public class DigitalBankingController {

    private final DigitalFundTransferable transferService;

    public DigitalBankingController(DigitalFundTransferable transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/neft")
    public ResponseEntity<TransactionReceipt> transferNeft(@RequestBody FundTransferRequest request) {
        return ResponseEntity.ok(transferService.transferNeft(request));
    }

    @PostMapping("/imps")
    public ResponseEntity<TransactionReceipt> transferImps(@RequestBody FundTransferRequest request) {
        return ResponseEntity.ok(transferService.transferImps(request));
    }
}
'@ | Set-Content -Path "$base\controller\DigitalBankingController.java" -Encoding UTF8

@'
package com.example.isp.controller;

import com.example.isp.contract.CreditFacilityManageable;
import com.example.isp.dto.CreditAdjustmentRequest;
import com.example.isp.dto.TransactionReceipt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/branch/credit")
public class BranchCreditController {

    private final CreditFacilityManageable creditService;

    public BranchCreditController(CreditFacilityManageable creditService) {
        this.creditService = creditService;
    }

    @PostMapping("/overdraft")
    public ResponseEntity<TransactionReceipt> adjustOverdraft(@RequestBody CreditAdjustmentRequest request) {
        return ResponseEntity.ok(creditService.adjustOverdraftLimit(request));
    }
}
'@ | Set-Content -Path "$base\controller\BranchCreditController.java" -Encoding UTF8

# 8. README.md
@'
# Enterprise BFSI — Interface Segregation Principle (ISP) Showcase

A production-grade Spring Boot 3 reference implementation demonstrating the **Interface Segregation Principle (ISP)** within a Banking, Financial Services, and Insurance (BFSI) architecture.

## Architectural Overview
The **Interface Segregation Principle (ISP)** states:
> *"Clients should not be forced to depend on interfaces they do not use."*

In enterprise banking systems, delivery channels operate under distinct boundaries:
- **ATM Terminals:** Only cash dispensation (`CashWithdrawable`).
- **Digital Banking:** Routing NEFT/IMPS transfers (`DigitalFundTransferable`).
- **Branch Operations:** Overdraft and lending facilities (`CreditFacilityManageable`).

## Verification Commands
### ATM Cash Withdrawal:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/atm/withdraw" `
  -Method Post `
  -Headers @{ "Content-Type" = "application/json" } `
  -Body '{"accountId": "ACC-990011", "atmTerminalId": "ATM-DEL-04", "amount": 2500.00}'
